package com.example.car_racing_betting_game_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.io.File;

public class WinGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_win_game);

        // Detail
        ImageView image = findViewById(R.id.imageViewWin);

        int gifResourceId = R.drawable.winwaowao;

        Glide.with(this)
                .asGif()
                .load(gifResourceId)
                .into(image);

        // Background
        RelativeLayout imageViewBackground = findViewById(R.id.totalView);
        ImageView imageView = new ImageView(this);
        imageViewBackground.addView(imageView);
        Glide.with(this)
                .asGif()
                .load(R.drawable.giphy)
                .into(imageView);

        image.bringToFront();

        TextView bonus = findViewById(R.id.textView);

        Intent intent = getIntent();
        String data = intent.getStringExtra("point");
        bonus.setText( "+ " + data);
        bonus.bringToFront();


        Button playAgian = (Button) findViewById(R.id.buttonPlayAgain);
        playAgian.setOnClickListener(view -> {
            finish();
        });


    }
}