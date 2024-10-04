package com.example.project1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

/**
 * GameActivity for the Whack-A-Mole game.
 */
public class GameActivity extends AppCompatActivity {

    /**
     * View model for the game.
     */
    private WhackAMoleViewModel viewModel;

    /**
     * Array of all the mole ImageViews.
     */
    private ImageView[] moles;

    /**
     * Array of all the heart ImageViews.
     */
    private ImageView[] hearts;

    /**
     * Array of all the heart ImageViews (Lives).
     */
    private TextView scoreTextView;

    /**
     * Array of all the mole ImageViews.
     */
    private int[] moleIds = {
            R.id.game_mole1_iv, R.id.game_mole2_iv, R.id.game_mole3_iv,
            R.id.game_mole4_iv, R.id.game_mole5_iv, R.id.game_mole6_iv,
            R.id.game_mole7_iv, R.id.game_mole8_iv, R.id.game_mole9_iv
    };

    /**
     * Array of all the heart ImageViews.
     */
    private int[] heartIds = {
            R.id.game_life1_iv, R.id.game_life2_iv, R.id.game_life3_iv
    };

    /**
     * Called when the activity is first created.
     * Initializes the game and sets up the UI.
     * Creates the view model to preserve state across orientation changes.
     * Creates 9 element array for moles and 3 element array for lies
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(WhackAMoleViewModel.class);
        moles = new ImageView[9];
        hearts = new ImageView[3];  // For displaying lives as hearts
        scoreTextView = findViewById(R.id.game_score_tv);  // Initialize score TextView

        // Initialize the heart ImageViews (for lives)
        for (int i = 0; i < heartIds.length; i++) {
            hearts[i] = findViewById(heartIds[i]);
        }

        // Initialize all mole ImageViews
        for (int i = 0; i < moleIds.length; i++) {
            moles[i] = findViewById(moleIds[i]);
            final int index = i;
            moles[i].setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when a mole is clicked.
                 * Checks that the game is still going and the mole that ic clicked is visible.
                 * Hides the mole and increments the score.
                 * @param view view
                 */
                @Override
                public void onClick(View view) {
                    if (!viewModel.isGameEnded() && viewModel.getCurrentMole() == index) { // Only whack if this is the active mole
                        moles[index].setImageResource(R.drawable.nomole); // Whack it and hide
                        viewModel.hitMole(); // Increment score in ViewModel
                        updateScore(); // Update the UI to reflect new score
                    }
                }
            });
        }

        // Start the game and moles automatically pop up
        viewModel.startGame();

        // Set up a periodic task to update the UI based on which mole is active
        final Handler uiHandler = new Handler(Looper.getMainLooper());
        Runnable uiRunnable = new Runnable() {
            /**
             * Called every 500 ms to update the UI.
             */
            @Override
            public void run() {
                // Check if game has ended
                if (viewModel.isGameEnded()) {
                    saveHighScore();  // Save high score if the game ends
                    showGameOver();
                    return;
                }

                // Reset all moles to empty images
                for (ImageView mole : moles) {
                    mole.setImageResource(R.drawable.nomole);
                }

                // Only show the active mole
                int currentMole = viewModel.getCurrentMole();
                if (currentMole != -1) {
                    moles[currentMole].setImageResource(R.drawable.mole);
                }

                // Update lives and score in UI
                updateScore();
                updateLives();  // Update the heart icons based on remaining lives

                // Keep updating the UI every 500ms (adjust as necessary)
                uiHandler.postDelayed(this, 500);
            }
        };
        uiHandler.post(uiRunnable);
    }

    /**
     * Function to update the score in the UI.
     */
    private void updateScore() {
        int score = viewModel.getScore();
        scoreTextView.setText("Score: " + score);  // Update the score TextView
    }

    /**
     * Function to update the lives in the UI.
     */
    private void updateLives() {
        int lives = viewModel.getLives();
        for (int i = 0; i < hearts.length; i++) {
            if (i < lives) {
                hearts[i].setImageResource(R.drawable.full_heart);  // Show full heart if lives are remaining
            } else {
                hearts[i].setImageResource(R.drawable.empty_heart);  // Show empty heart if life is lost
            }
        }
    }

    /**
     * Function to show the game over screen and end the game.
     */
    private void showGameOver() {
        Toast.makeText(this, "Game Over! Your Score: " + viewModel.getScore(), Toast.LENGTH_LONG).show();

        // Optionally, navigate to a Game Over screen or restart the game
        // For example, finish() to go back to the main menu:
        finish(); // Close the GameActivity and return to the main menu
    }

    /**
     * Function to save the high score using SharedPreferences.
     */
    private void saveHighScore() {
        SharedPreferences prefs = getSharedPreferences("WhackAMolePrefs", MODE_PRIVATE);
        int highScore = prefs.getInt("high_score", 0);

        int currentScore = viewModel.getScore();
        if (currentScore > highScore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("high_score", currentScore);
            editor.apply();
        }
    }

    /**
     * Called when the activity is destroyed.
     * Stops the game in the view model.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.stopGame(); // Stop the mole popping logic when the activity is destroyed
    }
}
