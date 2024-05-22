package com.example.car_racing_betting_game_mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Random;

public class BettingPageActivity extends AppCompatActivity {

    private Random random = new Random();
    private String username;
    private boolean canAddCoins;
    private int timeLeft = 0;
    private int totalCoins = 0;
    private SeekBar seekBar1, seekBar2, seekBar3;
    private final int RESPONSE_CODE_B = 1;
    private CheckBox cbCar1, cbCar2, cbCar3;
    private EditText edCar1, edCar2, edCar3;
    private TextView tvPoint;
    Button btnStart, btnReset, btnRule;
    ImageButton btnAddMoney;
    ImageButton btnBack;
    private boolean stop = false;
    private double PointWin;
    private double PointLost;
    private double PointBetting;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting);

        // get data from information user activity
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        totalCoins = intent.getIntExtra("balance", 0);
        canAddCoins = intent.getBooleanExtra("canAddCoins", true);
        timeLeft = intent.getIntExtra("timeLeft", 0);

        if (username != null && totalCoins != 0) {
            tvPoint = findViewById(R.id.tvPoint);
            tvPoint.setText("" + totalCoins);
        }
        // get view
        btnAddMoney = findViewById(R.id.btnAddMoney);
        btnStart = findViewById(R.id.btnStart);
        btnBack = findViewById(R.id.backBtn);
        btnReset = findViewById(R.id.btnReset);
        btnRule = findViewById(R.id.btnRule);
        seekBar1 = findViewById(R.id.sbCar1);
        seekBar2 = findViewById(R.id.sbCar2);
        seekBar3 = findViewById(R.id.sbCar3);
        cbCar1 = findViewById(R.id.cbCar1);
        cbCar2 = findViewById(R.id.cbCar2);
        cbCar3 = findViewById(R.id.cbCar3);
        edCar1 = findViewById(R.id.edCar1);
        edCar2 = findViewById(R.id.edCar2);
        edCar3 = findViewById(R.id.edCar3);
        tvPoint = findViewById(R.id.tvPoint);

        btnReset.setOnClickListener(view -> {
            ableInputs();
            SetPointsBetting();
            seekBar1.setProgress(0);
            seekBar2.setProgress(0);
            seekBar3.setProgress(0);
            stop = true;
        });
        btnStart.setOnClickListener(view -> {
            if (!tvPoint.getText().toString().isEmpty()) {
                double Points = Double.parseDouble(tvPoint.getText().toString());
                if (Points == 0) {
                    Toast.makeText(BettingPageActivity.this,
                            "Do not money to play game! Add money more! ", Toast.LENGTH_SHORT).show();
                } else {
                    if (validateInputs()) {
                        seekBar1.setProgress(0);
                        seekBar2.setProgress(0);
                        seekBar3.setProgress(0);
                        stop = false;
                        disableInputs();
                        runRandomFunctions();
                    } else {
                        SetPointsBetting();
                    }
                }
            }
        });
        /**
         * Add money and redirect to user information page
         */
        btnAddMoney.setOnClickListener(view -> {
            showAddCoinsDialog();
        });
        btnBack.setOnClickListener(view -> {
            Intent intent1 = configureStore(this, InformationUserActivity.class);
            setResult(RESPONSE_CODE_B, intent1);
            finish();
        });
        btnRule.setOnClickListener(view -> {
            Intent intent1 = configureStore(this, RulesPageActivity.class);
            launcherC.launch(intent1);
        });
    }

    ActivityResultLauncher<Intent> launcherC = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // get request from C
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        username = data.getStringExtra("username");
                        totalCoins = data.getIntExtra("balance", 0);
                        canAddCoins = data.getBooleanExtra("canAddCoins", true);
                        timeLeft = data.getIntExtra("timeLeft", 0);
                    }
                }
            });

    private boolean validateInputs() {
        double totalAllowedValue = Double.parseDouble(tvPoint.getText().toString());
        int value1 = cbCar1.isChecked() ?
                (edCar1.getText().toString().isEmpty() ? 0 :
                        Integer.parseInt(edCar1.getText().toString())) : 0;
        int value2 = cbCar2.isChecked() ?
                (edCar2.getText().toString().isEmpty() ? 0 :
                        Integer.parseInt(edCar2.getText().toString())) : 0;
        int value3 = cbCar3.isChecked() ?
                (edCar3.getText().toString().isEmpty() ? 0 :
                        Integer.parseInt(edCar3.getText().toString())) : 0;

        if ((cbCar1.isChecked() && value1 > totalAllowedValue) ||
                (cbCar2.isChecked() && value2 > totalAllowedValue) ||
                (cbCar3.isChecked() && value3 > totalAllowedValue)) {
            Toast.makeText(this,
                    "Giá trị của một trong các Car vượt quá giá trị cho phép.", Toast.LENGTH_LONG).show();
            return false;
        }
        int totalValue = value1 + value2 + value3;
        if (totalValue > totalAllowedValue) {
            Toast.makeText(this,
                    "Tổng giá trị của các Car vượt quá giá trị cho phép.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (value1 == 0 && value2 == 0 && value3 == 0) {
            Toast.makeText(this,
                    "Bạn chưa chọn xe nào để chạy!  .", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void SetPointsBetting() {
        edCar1.setText("");
        edCar2.setText("");
        edCar3.setText("");
        cbCar1.setChecked(false);
        cbCar2.setChecked(false);
        cbCar3.setChecked(false);
    }

    private void disableInputs() {
        cbCar1.setEnabled(false);
        cbCar2.setEnabled(false);
        cbCar3.setEnabled(false);
        edCar1.setEnabled(false);
        edCar2.setEnabled(false);
        edCar3.setEnabled(false);
    }

    private void ableInputs() {
        cbCar1.setEnabled(true);
        cbCar2.setEnabled(true);
        cbCar3.setEnabled(true);
        edCar1.setEnabled(true);
        edCar2.setEnabled(true);
        edCar3.setEnabled(true);
    }

    private void runRandomFunctions() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                double totalAllowedValue = Double.parseDouble(tvPoint.getText().toString());
                int value1 = cbCar1.isChecked() ?
                        (edCar1.getText().toString().isEmpty() ? 0 :
                                Integer.parseInt(edCar1.getText().toString())) : 0;
                int value2 = cbCar2.isChecked() ?
                        (edCar2.getText().toString().isEmpty() ? 0 :
                                Integer.parseInt(edCar2.getText().toString())) : 0;
                int value3 = cbCar3.isChecked() ?
                        (edCar3.getText().toString().isEmpty() ? 0 :
                                Integer.parseInt(edCar3.getText().toString())) : 0;
                if (!stop) {
                    int car1 = seekBar1.getProgress();
                    int car2 = seekBar2.getProgress();
                    int car3 = seekBar3.getProgress();
                    if (car1 < 100 && car2 < 100 && car3 < 100) {
                        car1 += random.nextInt(6);
                        car2 += random.nextInt(6);
                        car3 += random.nextInt(6);

                        seekBar1.setProgress(Math.min(car1, 100));
                        seekBar2.setProgress(Math.min(car2, 100));
                        seekBar3.setProgress(Math.min(car3, 100));
                        handler.postDelayed(this, 1000);
                    }
                    if (car1 >= 100 || car2 >= 100 || car3 >= 100) {
                        String message = "Car là xe chiến thắng ";
                        if (car1 >= 100) {
                            PointBetting = (value1 + value2 + value3);
                            PointWin = value1 * 2;
                            message = "Car 1 là xe chiến thắng";
                        } else if (car2 >= 100) {
                            PointBetting = (value1 + value2 + value3);
                            PointWin = value2 * 2;
                            message = "Car 2 là xe chiến thắng";
                        } else if (car3 >= 100) {
                            PointBetting = (value1 + value2 + value3);
                            PointWin = value3 * 2;
                            message = "Car 3 là xe chiến thắng";
                        }

                        Toast.makeText(BettingPageActivity.this, message, Toast.LENGTH_SHORT).show();

                        double pointCurrent = Double.parseDouble(tvPoint.getText().toString());
                        double point = (pointCurrent - PointBetting) + PointWin;
                        double pointNoti;
                        if (point >= pointCurrent) {
                            pointNoti = point - totalAllowedValue;
                            tvPoint.setText("" + point);
                            totalCoins = (int) point;
                            Intent intent = new Intent(BettingPageActivity.this, WinGame.class);
                            intent.putExtra("point", pointNoti + "");
                            startActivity(intent);
                        } else {
                            pointNoti = totalAllowedValue - point;
                            tvPoint.setText("" + point);
                            totalCoins = (int) point;
                            Intent intent = new Intent(BettingPageActivity.this, LoseGame.class);
                            intent.putExtra("point", pointNoti + "");
                            startActivity(intent);
                        }
                        stop = true;
                    } else {
                        handler.postDelayed(this, 1000);
                    }
                }
            }
        };
        handler.post(runnable);
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
                    tvPoint.setText("" + totalCoins);
                    canAddCoins = false; // if user clicked on button get coins -> set canAddCoins to false
                    timeLeft = 1 * 60 * 1000; // 1 minute in milliseconds
                    startCountDownTimer(timeLeft); // set count down from main activity
                } else {
                    Toast.makeText(BettingPageActivity.this, "Please enter a value between 1 and 100", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addCoinsDialogFragment.show(fragmentManager, "add_coins_dialog");
    }

    public Intent configureStore(Context packageContext, Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);
        intent.putExtra("username", username);
        intent.putExtra("balance", totalCoins);
        intent.putExtra("canAddCoins", canAddCoins);
        intent.putExtra("timeLeft", timeLeft);
        return intent;
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
                Toast.makeText(BettingPageActivity.this, "You can add coins again", Toast.LENGTH_SHORT).show();
            }
        };
        countDownTimer.start();
    }
}