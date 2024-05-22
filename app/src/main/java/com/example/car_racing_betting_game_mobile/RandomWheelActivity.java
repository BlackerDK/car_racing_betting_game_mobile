package com.example.car_racing_betting_game_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class RandomWheelActivity extends AppCompatActivity {
    private ImageView wheel;
    private TextView remainingSpinText;
    private Button spinButton;
    private ImageButton nextButton;
    private static final  String[] sectors = { "100","15", "40", "20", "0", "70", "40", "jackpot"};
    private  static  final  int [] sectorsDegree = new int[sectors.length];
    private static final Random random = new Random();
    private int degree = 0;
    private int remainingSpin = 3;
    private String username = "";
    private int balance = 0;
    private boolean isSpinning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_random_wheel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        balance = intent.getIntExtra("balance", 0);
        remainingSpin = intent.getIntExtra("remainingSpin", 3);
        nextButton = findViewById(R.id.nextSectionButton);
        wheel = findViewById(R.id.imageWheel);
        spinButton = findViewById(R.id.btnSpin);
        remainingSpinText = findViewById(R.id.txtRemainingSpinNumber);
        getSectorDegree();
        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSpinning && remainingSpin > 0){
                    isSpinning = true;
                    spinWheel();
                }
            }

        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RandomWheelActivity.this, InformationUserActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("balance", balance);
                intent.putExtra("timeLeft",0);
                startActivity(intent);
                finish();
            }
        });

    }
    private void getSectorDegree(){
        int sectorDegree = 360/sectors.length;
        for (int i = 0; i < sectors.length; i++) {
            sectorsDegree[i] = sectorDegree * (i+1);
        }
    }
    private void spinWheel() {
        degree = random.nextInt(sectors.length - 1);
        RotateAnimation rotateAnimation = new RotateAnimation(0, (360 * sectors.length) + sectorsDegree[degree],
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(3600);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(remainingSpin == 0){
                    spinButton.setVisibility(View.GONE);
                    return;
                }
                if(sectors[sectors.length - (degree + 1)].equals("jackpot")){
                    Toast.makeText(RandomWheelActivity.this, "You won Jackpot", Toast.LENGTH_SHORT).show();
                    remainingSpin = 10;
                    remainingSpinText.setText("Remaining Spin: " + remainingSpin);
                    isSpinning = false;
                    return;
                }
                int money = Integer.parseInt(sectors[sectors.length - (degree + 1)]);
                Toast.makeText(RandomWheelActivity.this, "You won " + money , Toast.LENGTH_SHORT).show();
                --remainingSpin;
                remainingSpinText.setText("Remaining Spin: " + remainingSpin);
                isSpinning = false;
                balance += money;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        wheel.startAnimation(rotateAnimation);
    }
}
