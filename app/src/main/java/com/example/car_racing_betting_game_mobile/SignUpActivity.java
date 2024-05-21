package com.example.car_racing_betting_game_mobile;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private TextView signUp;
    private TextView alreadyHaveAccount;

    private  final String REQUIRE = "Require";
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = (TextInputEditText) findViewById(R.id.username);
        etPassword =(TextInputEditText) findViewById(R.id.password);
        etConfirmPassword =(TextInputEditText) findViewById(R.id.passwordConfirm);
        signUp = (TextView) findViewById(R.id.signUp);
        alreadyHaveAccount = (TextView) findViewById(R.id.alreadyHaveAccount);

        alreadyHaveAccount.setOnClickListener(this);
        signUp.setOnClickListener(this);

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
        if (TextUtils.isEmpty(etConfirmPassword.getText().toString())){
            etConfirmPassword.setError(REQUIRE);
            return false;
        }
        if (!TextUtils.equals(etPassword.getText().toString()
                ,etConfirmPassword.getText().toString())){
            Toast.makeText(this,"Password are not match",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private void signUp(){
        if(!checkInput()){
            return;
        }else{
            Toast.makeText(this,"Sign up success",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,InformationUserActivity.class);
            intent.putExtra("username",etUsername.getText().toString());
            // After logged in -> Automatically bonus 100 coins to user
            intent.putExtra("balance",100);
            startActivity(intent);
            finish();
        }
    }
    private void signInForm(){
        Intent intent = new Intent(this,SignInActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onClick(View v){
        if (v.getId() == R.id.signUp) {
            signUp();
        } else if (v.getId() == R.id.alreadyHaveAccount) {
            signInForm();
        }
    }
}
