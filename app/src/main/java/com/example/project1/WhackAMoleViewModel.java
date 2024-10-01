package com.example.project1;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.ViewModel;

import java.util.Random;

public class WhackAMoleViewModel extends ViewModel {

    private int score = 0;
    private int lives = 3; // Start with 3 lives
    private final Random random = new Random();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable moleRunnable;
    private int currentMole = -1; // -1 means no mole is visible
    private boolean moleWhacked = false; // Flag to check if mole was whacked
    private boolean gameEnded = false; // Flag to check if the game has ended
    private int difficultyLevel = 1; // Starts at level 1
    private long moleDelay = 1500; // Initial delay of 1.5 seconds between moles

    public int getScore() {
        return score;
    }

    public int getCurrentMole() {
        return currentMole;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void hitMole() {
        if (!gameEnded) {
            score += 10;
            moleWhacked = true; // Mole was hit
            currentMole = -1; // No mole is visible after it's hit

            // Increase difficulty as score increases
            increaseDifficulty();
        }
    }

    public void loseLives() {
        if (!gameEnded) {
            lives -= 1;
            if (lives <= 0) {
                endGame();
            }
        }
    }

    // Increase difficulty by reducing moleDelay and making it harder
    private void increaseDifficulty() {
        difficultyLevel++;

        // Reduce delay to make moles pop up faster
        if (difficultyLevel % 5 == 0 && moleDelay > 500) { // Every 5 levels, reduce delay
            moleDelay -= 100; // Decrease the delay between moles by 200ms
        }
    }

    // Start the game, ensuring moles pop up at regular intervals with increasing difficulty
    public void startGame() {
        gameEnded = false;
        moleRunnable = new Runnable() {
            @Override
            public void run() {
                if (!moleWhacked && currentMole != -1) {
                    // Mole was not whacked before popping down, reduce lives
                    loseLives();
                }

                if (!gameEnded) {
                    // Choose a new random mole (0 to 8)
                    currentMole = random.nextInt(9);
                    moleWhacked = false; // Reset flag for the new mole

                    // Schedule the next mole pop-up after the current delay
                    handler.postDelayed(this, moleDelay);
                }
            }
        };
        handler.post(moleRunnable); // Start the first mole pop-up immediately
    }

    public void stopGame() {
        handler.removeCallbacks(moleRunnable);
    }

    // End the game by stopping the mole popping logic
    private void endGame() {
        gameEnded = true;
        stopGame();
    }
}
