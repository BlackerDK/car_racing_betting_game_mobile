package com.example.car_racing_betting_game_mobile;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class BettingPageActivity extends AppCompatActivity {

    private Random random = new Random();
    private SeekBar seekBar1, seekBar2, seekBar3;
    private CheckBox cbCar1,cbCar2, cbCar3;
    private EditText edCar1, edCar2,edCar3;
    private TextView tvPoint;
    Button btnStart, btnReset;
    private boolean stop = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting);
        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);
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
            stop=true;
        });
        btnStart.setOnClickListener(view -> {
            if (!tvPoint.getText().toString().isEmpty()){
                double Points = Double.parseDouble(tvPoint.getText().toString());
                if (Points == 0){
                    Toast.makeText(BettingPageActivity.this,
                            "Do not money to play game! Add money more! ", Toast.LENGTH_SHORT).show();
                }else{
                    if (validateInputs()){
                        seekBar1.setProgress(0);
                        seekBar2.setProgress(0);
                        seekBar3.setProgress(0);
                        stop=false;
                        disableInputs();
                        runRandomFunctions();
                    }else{
                        SetPointsBetting();
                    }
                }
            }
        });
    }
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
        } else {
            return true;
        }
    }
    private void SetPointsBetting(){
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
                if(!stop) {
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
                            totalAllowedValue = totalAllowedValue /*đây là giá trị ban đầu*/ - (value1+value2+value3); //tổng tiền đặt;
                            double PointWin = totalAllowedValue + value1 * 2;// Tổng số tiền còn khi đặt xong + thắng tiền car cược
                            tvPoint.setText(PointWin+"");
                            message = "Car 1 là xe chiến thắng";
                        } else if (car2 >= 100) {
                            totalAllowedValue = totalAllowedValue - (value1+value2+value3);
                            double PointWin = totalAllowedValue + value2 * 2;
                            tvPoint.setText(PointWin+"");
                            message = "Car 2 là xe chiến thắng";
                        } else if (car3 >= 100) {
                            totalAllowedValue = totalAllowedValue - (value1+value2+value3);
                            double PointWin = totalAllowedValue + value3 * 2;
                            tvPoint.setText(PointWin+"");
                            message = "Car 3 là xe chiến thắng";
                        }
                        Toast.makeText(BettingPageActivity.this, message, Toast.LENGTH_SHORT).show();
                        stop = true;
                    } else {
                        handler.postDelayed(this, 1000);
                    }
                }
            }
        };
        handler.post(runnable);
    }
}