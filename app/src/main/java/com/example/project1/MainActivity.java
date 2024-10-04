package com.example.project1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * MainActivity for the Whack-A-Mole game.
 * From this screen the user can view their high score or start a new game.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * TextView to display high score.
     */
    private TextView highScoreTextView;  // TextView to display high score

    /**
     * Called when the activity is first created.
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button newGameBtn = findViewById(R.id.main_new_game_btn);
        highScoreTextView = findViewById(R.id.main_high_score_tv);  // Initialize high score TextView

        // Load and display the high score (on creation)
        loadHighScore();

        newGameBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the button is clicked.
             * Starts a new game activity and switched to it.
             * @param view view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Called when the activity is resumed.
     * Loads high score on resume.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Reload the high score every time the activity is resumed
        loadHighScore();
    }

    /**
     * Loads the high score from SharedPreferences and updates the TextView.
     */
    private void loadHighScore() {
        SharedPreferences prefs = getSharedPreferences("WhackAMolePrefs", MODE_PRIVATE);
        int highScore = prefs.getInt("high_score", 0);

        // Dynamically update the high score TextView
        highScoreTextView.setText(getString(R.string.session_score) + " " + highScore);
    }
}
