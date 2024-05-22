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
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        int totalCoins = intent.getIntExtra("balance", 0);
        boolean canAddCoins = intent.getBooleanExtra("canAddCoins", true);
        int timeLeft = intent.getIntExtra("timeLeft", 0);
        back.setOnClickListener(v -> {
            Intent onBackIntent = new Intent(RulesPageActivity.this, BettingPageActivity.class);
            onBackIntent.putExtra("username", username);
            onBackIntent.putExtra("balance", totalCoins);
            onBackIntent.putExtra("canAddCoins", canAddCoins);
            onBackIntent.putExtra("timeLeft", timeLeft);
            setResult(RESULT_OK, onBackIntent);
            finish();
        });
    }
}
