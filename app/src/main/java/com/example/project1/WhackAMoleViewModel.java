package com.example.project1;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.ViewModel;

import java.util.Random;

/**
 * ViewModel for the Whack-A-Mole game.
 */
public class WhackAMoleViewModel extends ViewModel {

    /**
     * int for score (+10 for every hit mole).
     */
    private int score = 0;

    /**
     * int for lives (-1 for every missed mole).
     * Start with 3 lives.
     */
    private int lives = 3;

    /**
     * Random object to generate random numbers.
     */
    private final Random random = new Random();

    /**
     * Handler to schedule the next mole pop-up.
     */
    private final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Runnable to schedule the next mole pop-up.
     */
    private Runnable moleRunnable;

    /**
     * int for current mole.
     * -1 means no mole is visible.
     */
    private int currentMole = -1;

    /**
     * boolean flag to check if mole was whacked.
     */
    private boolean moleWhacked = false;

    /**
     * boolean flag to check if the game has ended.
     */
    private boolean gameEnded = false;

    /**
     * int for difficulty level.
     * Starts at level 1.
     */
    private int difficultyLevel = 1;

    /**
     * long for mole delay.
     * starts at 1500 or 1.5 seconds
     */
    private long moleDelay = 1500;

    /**
     * Get the current score.
     * @return this score
     */
    public int getScore() {
        return score;
    }

    /**
     * Get the current mole.
     * @return this mole
     */
    public int getCurrentMole() {
        return currentMole;
    }

    /**
     * Get the current lives.
     * @return this lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Check if the game has ended.
     * @return this gameEnded
     */
    public boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * Increment the score by 10 if mole is hit.
     * Then increase difficulty and reset current mole to -1.
     */
    public void hitMole() {
        if (!gameEnded) {
            score += 10;
            moleWhacked = true; // Mole was hit
            currentMole = -1; // No mole is visible after it's hit

            // Increase difficulty as score increases
            increaseDifficulty();
        }
    }

    /**
     * Decrement the lives by 1 if mole is missed.
     * End game if lives = 0.
     */
    public void loseLives() {
        if (!gameEnded) {
            lives -= 1;
            if (lives <= 0) {
                endGame();
            }
        }
    }

    /**
     * Increase the difficulty level and adjust the mole delay.
     */
    private void increaseDifficulty() {
        difficultyLevel++;

        // Reduce delay to make moles pop up faster
        if (difficultyLevel % 5 == 0 && moleDelay > 500) { // Every 5 levels, reduce delay
            moleDelay -= 100; // Decrease the delay between moles by 200ms
        }
    }

    /**
     * Start the game by scheduling the first mole pop-up.
     */
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

    /**
     * Stop the game by removing the mole pop-up.
     */
    public void stopGame() {
        handler.removeCallbacks(moleRunnable);
    }

    /**
     * End the game by stopping the mole popping logic.
     */
    private void endGame() {
        gameEnded = true;
        stopGame();
    }
}
