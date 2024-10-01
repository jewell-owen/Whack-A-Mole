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

public class GameActivity extends AppCompatActivity {

    private WhackAMoleViewModel viewModel;
    private ImageView[] moles;
    private ImageView[] hearts;  // ImageView array for the hearts (lives)
    private TextView scoreTextView;  // TextView for displaying the score
    private int[] moleIds = {
            R.id.game_mole1_iv, R.id.game_mole2_iv, R.id.game_mole3_iv,
            R.id.game_mole4_iv, R.id.game_mole5_iv, R.id.game_mole6_iv,
            R.id.game_mole7_iv, R.id.game_mole8_iv, R.id.game_mole9_iv
    };
    private int[] heartIds = {
            R.id.game_life1_iv, R.id.game_life2_iv, R.id.game_life3_iv
    };

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

    // Method to update score in the UI
    private void updateScore() {
        int score = viewModel.getScore();
        scoreTextView.setText("Score: " + score);  // Update the score TextView
    }

    // Method to update lives (hearts) in the UI
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

    // Show a game over message when the game ends
    private void showGameOver() {
        Toast.makeText(this, "Game Over! Your Score: " + viewModel.getScore(), Toast.LENGTH_LONG).show();

        // Optionally, navigate to a Game Over screen or restart the game
        // For example, finish() to go back to the main menu:
        finish(); // Close the GameActivity and return to the main menu
    }

    // Save the high score using SharedPreferences
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.stopGame(); // Stop the mole popping logic when the activity is destroyed
    }
}
