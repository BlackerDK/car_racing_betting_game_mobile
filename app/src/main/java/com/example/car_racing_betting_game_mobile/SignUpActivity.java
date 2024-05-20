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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private TextView tvAlreadyAccount;
    private Button btnSignUp;

    private  final String REQUIRE="Require";
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = (EditText) findViewById(R.id.txtUseName);
        etPassword =(EditText) findViewById(R.id.txtPassword);
        etConfirmPassword =(EditText) findViewById(R.id.txtConfirmPassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        tvAlreadyAccount = (TextView) findViewById(R.id.tvAccountAlready);

        tvAlreadyAccount.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
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
        if (v.getId() == R.id.btnSignUp) {
            signUp();
        } else if (v.getId() == R.id.tvAccountAlready) {
            signInForm();
        }
    }
}
