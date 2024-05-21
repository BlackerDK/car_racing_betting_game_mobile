package com.example.car_racing_betting_game_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RulesPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_rules);
        Button back = findViewById(R.id.btn_Back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, BettingPageActivity.class);
            startActivity(intent);
        });
    }
}
