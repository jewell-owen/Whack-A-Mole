package com.example.project1;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class WhackAMoleViewModel extends ViewModel{

    private int score;
    private int lives;
    private long difficulty = 0L;
    private long nextTime;
    Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    /**
     * This increase the difficulty
     */
    public void setDifficulty(){
        nextTime = SystemClock.uptimeMillis() + 1000;
        runnable = new Runnable() {
            @Override
            public void run() {
                difficulty += 1;
                nextTime += 1000;
                handler.postAtTime(this, nextTime);
            }
        };
        handler.postAtTime(runnable, nextTime);

    }

    public int getScore() {
        return score;
    }

    public void hitMole(){
        score += 10;
    }

    public void setLives(){
        lives = 3;
    }

    public int getLives(){
        return lives;
    }

    public void loseLives(){
        lives -= 1;
    }

}
