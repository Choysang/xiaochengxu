package com.example.androiddingjing.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androiddingjing.R;

public class QueryInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_info);

        EditText account = findViewById(R.id.query_account);
        EditText nickname = findViewById(R.id.query_nickname);
        EditText email = findViewById(R.id.query_email);
        TextView returnBtn = findViewById(R.id.queryReturnMine);//返回按钮

        returnBtn.setOnClickListener(v -> {
            finish();
        });

        SharedPreferences read = this.getSharedPreferences("AccountInfo", Context.MODE_PRIVATE);
        account.setText(read.getString("account", null));
        nickname.setText(read.getString("name", null));
        email.setText(read.getString("email", null));

    }
}