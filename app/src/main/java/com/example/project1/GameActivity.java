package com.example.project1;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity {

    private ImageView mole1;
    private ImageView mole2;
    private ImageView mole3;
    private ImageView mole4;
    private ImageView mole5;
    private ImageView mole6;
    private ImageView mole7;
    private ImageView mole8;
    private ImageView mole9;


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

        mole1 = findViewById(R.id.game_mole1_iv);
        mole2 = findViewById(R.id.game_mole1_iv);
        mole3 = findViewById(R.id.game_mole1_iv);
        mole4 = findViewById(R.id.game_mole1_iv);
        mole5 = findViewById(R.id.game_mole1_iv);
        mole6 = findViewById(R.id.game_mole1_iv);
        mole7 = findViewById(R.id.game_mole1_iv);
        mole8 = findViewById(R.id.game_mole1_iv);
        mole9 = findViewById(R.id.game_mole1_iv);

    }
}
