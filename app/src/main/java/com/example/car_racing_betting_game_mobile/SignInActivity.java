package com.example.car_racing_betting_game_mobile;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class SignInActivity  extends AppCompatActivity implements View.OnClickListener {

    private TextView signIn;
    private TextView signUp;

    private TextInputEditText etUsername;
    private TextInputEditText etPassword;


    private  final String REQUIRE="Require";
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_sign_in);

        etUsername = (TextInputEditText) findViewById(R.id.username);
        etPassword =(TextInputEditText) findViewById(R.id.password);
        signIn = (TextView) findViewById(R.id.signIn);
        signUp = (TextView) findViewById(R.id.signUp);

        signUp.setOnClickListener(this);
        signIn.setOnClickListener(this);
    }
    private boolean checkInput(){
        if(TextUtils.isEmpty(etUsername.getText().toString())){
            etUsername.setError(REQUIRE);
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText().toString())){
            etPassword.setError(REQUIRE);
            return false;
        }
        return true;
    }
    private void signIn(){
        if(!checkInput()){
            return;
        }

        Intent intent = new Intent(this,RandomWheelActivity.class);
        // After logged in -> Automatically bonus 100 coins to user
        int balance = getBalanceFromPreviousLoggedIn();
        intent.putExtra("username",etUsername.getText().toString());
        intent.putExtra("balance",balance);
        intent.putExtra("timeLeft",0);
        startActivity(intent);
        finish();
    }
    private void signUpForm(){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private int getBalanceFromPreviousLoggedIn(){
        String username = etUsername.getText().toString().trim();
        // get from previous saved game
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences",MODE_PRIVATE);
        return sharedPreferences.getInt(username,100);
    }
    @Override
    public void onClick(View v){
        if (v.getId() == R.id.signIn) {
            signIn();
        } else if (v.getId() == R.id.signUp) {
            signUpForm();
        }
    }
}
