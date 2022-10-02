package com.example.androiddingjing.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.androiddingjing.activities.ui.login.LoginFragment;
import com.example.androiddingjing.R;
import com.example.androiddingjing.activities.ui.SignUp.SignUpFragment;
import com.example.androiddingjing.activities.ui.test.TestFragment;


public class LoginAndRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);

        View btnLogin= findViewById(R.id.btnLogin);
        View btnSignUp= findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnLogin.callOnClick();

    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new Fragment();
        switch (v.getId()){
            case R.id.btnLogin :
                fragment = new LoginFragment();
                View btnSignUp= findViewById(R.id.btnSignUp);
                btnSignUp.setBackgroundResource(R.drawable.button_background);
                break;
            case R.id.btnSignUp:
                fragment = new SignUpFragment();
                View btnLogin= findViewById(R.id.btnLogin);
                btnLogin.setBackgroundResource(R.drawable.button_background);
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
        v.setBackgroundResource(R.drawable.button_background_selected);
        Log.d("v.getId()",v.toString());
    }


}