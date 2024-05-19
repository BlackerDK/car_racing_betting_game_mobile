package com.example.car_racing_betting_game_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class LoseGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lose_game);

        // Detail
        ImageView image = findViewById(R.id.imageViewWin);
        int gifResourceId = R.drawable.losehuhu;

        Glide.with(this)
                .asGif()
                .load(gifResourceId)
                .into(image);

        TextView bonus = findViewById(R.id.textView);
        Intent intent = getIntent();
        String data = intent.getStringExtra("point");
        bonus.setText("- "+data);
        bonus.bringToFront();



        Button playAgian = (Button) findViewById(R.id.buttonPlayAgain);
        playAgian.setOnClickListener(view -> {
            finish();
        });
    }
}