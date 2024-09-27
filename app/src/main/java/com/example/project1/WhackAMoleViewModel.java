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
}
