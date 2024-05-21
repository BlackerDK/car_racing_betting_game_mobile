package com.example.car_racing_betting_game_mobile;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class RandomWheelActivity extends AppCompatActivity {
    private ImageView wheel;
    private Button spinButton;
    private static final  String[] sectors = { "100","15", "40", "20", "0", "70", "40", "jackpot"};
    private  static  final  int [] sectorsDegree = new int[sectors.length];
    private static final Random random = new Random();
    private int degree = 0;
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

        wheel = findViewById(R.id.imageWheel);
        spinButton = findViewById(R.id.btnSpin);
        getSectorDegree();
        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSpinning){
                    isSpinning = true;
                    spinWheel();
                }
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
                Toast.makeText(RandomWheelActivity.this, "You won " + sectors[sectors.length - (degree + 1)], Toast.LENGTH_SHORT).show();
                isSpinning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        wheel.startAnimation(rotateAnimation);
    }
}
