package com.example.car_racing_betting_game_mobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


public class InformationUserActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvUsername, tvBalance;
    ImageButton logoutBtn, startBtn, addCoinBtn;
    private int totalCoins = 0;
    String usernameSaved;
    private static final int MAX_COINS = 100;
    private static final int REQUEST_CODE_B = 1;
    private static int timeLeft = 0; // x minutes in milliseconds
    private boolean canAddCoins = true;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_information);
        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        int balance = intent.getIntExtra("balance", 0);
        setInitialData(username, balance);
        // from BettingPages
        canAddCoins = intent.getBooleanExtra("canAddCoins", true);
        timeLeft = intent.getIntExtra("timeLeft", 0);


        tvUsername = findViewById(R.id.tvUsername);
        tvBalance = findViewById(R.id.tvBalance);
        logoutBtn = findViewById(R.id.logoutBtn);
        startBtn = findViewById(R.id.buttonStart);
        addCoinBtn = findViewById(R.id.coinBtn);

        tvUsername.setText("Welcome, " + username);
        tvBalance.setText("Your balance: " + balance + "$");
        logoutBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);
        addCoinBtn.setOnClickListener(this);

    }

    ActivityResultLauncher<Intent> laucherB = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == REQUEST_CODE_B) {
            Intent data = result.getData();
            if(data != null) {
                this.totalCoins = data.getIntExtra("balance", 0);
                this.tvBalance.setText("Your balance: " + this.totalCoins + "$");
                this.canAddCoins = data.getBooleanExtra("canAddCoins", true);
                this.timeLeft = data.getIntExtra("timeLeft", 0);
            }
        }
    });


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logoutBtn) {
            // Logout
            handleLogout();
        } else if (v.getId() == R.id.buttonStart) {
            // Start game
            navigateToRoadRacingCar();
        } else if (v.getId() == R.id.coinBtn) {
            // Add coin
            showAddCoinsDialog();
        }
    }

    public void setInitialData(String username, int balance) {
        if (username != null && balance != 0) {
            usernameSaved = username;
            totalCoins = balance;
            Toast.makeText(this, "Welcome to Road Racing Car", Toast.LENGTH_SHORT).show();
        }
    }

    public void navigateToRoadRacingCar() {
        Intent intent = new Intent(this, BettingPageActivity.class);
        intent.putExtra("username", usernameSaved);
        intent.putExtra("balance", totalCoins);
        intent.putExtra("timeLeft", timeLeft);
        intent.putExtra("canAddCoins", canAddCoins);
        //  start with activity B
        laucherB.launch(intent);
    }

    public void handleLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save game");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            /**
             * Save data to shared preferences
             * Redirect to sign in activity
             */
            saveData();
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    public void saveData() {
        // Save data
        // store username and balance to shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(usernameSaved, totalCoins); // username mapping to current balance
        editor.apply();
    }

    public void showAddCoinsDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // init dialog fragment with time left, which set to 0 at first
        TimeDialogFragment addCoinsDialogFragment = TimeDialogFragment.newInstance(timeLeft);
        addCoinsDialogFragment.setOnAddCoinsListener(new TimeDialogFragment.OnAddCoinsListener() {
            @Override
            public void onAddCoins(int coins) {
                if (canAddCoins) { // check whether user can add coins or not
                    totalCoins += coins;
                    tvBalance.setText("Your balance: " + totalCoins + "$");
                    canAddCoins = false; // if user clicked on button get coins -> set canAddCoins to false
                    timeLeft = 1 * 60 * 1000; // 1 minute in milliseconds
                    startCountDownTimer(timeLeft); // set count down from main activity
                } else {
                    Toast.makeText(InformationUserActivity.this, "Please enter a value between 1 and 100", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addCoinsDialogFragment.show(fragmentManager, "add_coins_dialog");
    }

    private void startCountDownTimer(long duration) {
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = (int) millisUntilFinished; // set time left to millisUntilFinished -> on tracking realtime
            }

            @Override
            public void onFinish() {
                // after time left is finished, set canAddCoins to true
                canAddCoins = true;
                timeLeft = 0;
                Toast.makeText(InformationUserActivity.this, "You can add coins again", Toast.LENGTH_SHORT).show();
            }
        };
        countDownTimer.start();
    }
}
