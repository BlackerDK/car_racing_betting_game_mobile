package com.example.car_racing_betting_game_mobile;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity  extends AppCompatActivity implements View.OnClickListener {
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvNotAccountYet;
    private Button btnSignIn;

    private  final String REQUIRE="Require";
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_sign_in);

        etUsername = (EditText) findViewById(R.id.txtUseName);
        etPassword =(EditText) findViewById(R.id.txtPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        tvNotAccountYet = (TextView) findViewById(R.id.tvNotAccountYet);

        tvNotAccountYet.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
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
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void signUpForm(){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onClick(View v){
        if (v.getId() == R.id.btnSignIn) {
            signIn();
        } else if (v.getId() == R.id.tvNotAccountYet) {
            signUpForm();
        }
    }
}
