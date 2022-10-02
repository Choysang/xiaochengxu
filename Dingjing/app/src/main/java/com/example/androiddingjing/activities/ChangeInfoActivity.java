package com.example.androiddingjing.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddingjing.R;
import com.example.androiddingjing.mysql.NetworkSettings;
import com.example.androiddingjing.mysql.NetworkThread;
import com.example.androiddingjing.mysql.ResponseBody;
import com.example.androiddingjing.tools.sendEmail;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.FutureTask;

public class ChangeInfoActivity extends AppCompatActivity {

    private int nowIdCode;//发送的验证码
    private String getCodeEmail;//最后一次成功发送验证码时输入的邮箱
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        TextView returnMine = findViewById(R.id.returnMine);
        returnMine.setOnClickListener(v -> {
            finish();
        });

        EditText account =  findViewById(R.id.change_account);//用户的账号
        EditText nickname =  findViewById(R.id.change_nickname);//输入的用户昵称
        EditText password =  findViewById(R.id.change_password);//输入的密码
        EditText rePassword =  findViewById(R.id.change_rePassword);//输入的确认密码
        EditText email =  findViewById(R.id.change_email);//输入的邮箱
        EditText idCode =  findViewById(R.id.change_idCode);//输入的验证码
        Button btn1 = findViewById(R.id.sendIdCodeBtn);//发送验证码的按钮
        Button confirmBtn = findViewById(R.id.change_confirm);//确认按钮

        /**
         * 读取本地存储的登录账号，并填入文本框内，同时禁止用户进行修改
         * */
        SharedPreferences read = this.getSharedPreferences("AccountInfo", Context.MODE_PRIVATE);
        account.setText(read.getString("account", null));

        /**
         * 验证码发送
         * */
        btn1.setOnClickListener(v -> {
            nowIdCode = (int) Math.round(Math.random() * (9999-1000) +1000);
            String result = sendEmail.send(email.getText().toString(),"定睛APP邮箱修改验证码","您的验证码是："+nowIdCode);
            if(result.equals("邮件不合法")){
                Toast.makeText(this, "您的邮箱格式错误，请检查！", Toast.LENGTH_LONG).show();
                return;
            }
            this.getCodeEmail=email.getText().toString();
            Toast.makeText(this, "验证码已发送，请查看邮箱", Toast.LENGTH_LONG).show();
            CountDownTimer mTimer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                   //Log.d("countDownTime","millisUntilFinished: " + millisUntilFinished);
                    btn1.setText(millisUntilFinished / 1000 + "秒后重发");
                }

                @Override
                public void onFinish() {
                    btn1.setEnabled(true);
                    btn1.setText("重新发送");
                    cancel();
                }
            };
            mTimer.start();
            btn1.setEnabled(false);
        });

        /**
         * 确认修改
         * */
        confirmBtn.setOnClickListener(v -> {
            if(nickname.getText().toString().isEmpty()&&password.getText().toString().isEmpty()&&email.getText().toString().isEmpty()){
                Toast.makeText(this, "您未修改任何信息", Toast.LENGTH_LONG).show();
                return;
            }

            //若输入了密码，则必须输入两个密码一致
            if(!password.getText().toString().isEmpty()&&!rePassword.getText().toString().equals(password.getText().toString())){
                Toast.makeText(this, "输入的两个密码不一致", Toast.LENGTH_LONG).show();
                return;
            }

            //若修改了绑定邮箱
            if(!email.getText().toString().isEmpty()){
                //发送验证码时的邮箱与现在填写的对应，验证码正确
               if(email.getText().toString().equals(getCodeEmail)&&idCode.getText().toString().equals(String.valueOf(nowIdCode))) {

               }else{
                   Toast.makeText(this, "验证码错误！", Toast.LENGTH_LONG).show();
                   return;
               }
            }

            //判断所有填入的内容是否为空，如果为空，则默认提交本地已保存的对应项
            String change_Name= nickname.getText().toString().isEmpty()?read.getString("name", null):nickname.getText().toString();
            String change_Account= account.getText().toString().isEmpty()?read.getString("account", null):account.getText().toString();
            String change_Password= password.getText().toString().isEmpty()?read.getString("password", null):password.getText().toString();
            String change_Email= email.getText().toString().isEmpty()?read.getString("email", null):email.getText().toString();

            Log.d("changeInfo",change_Name+" "+ change_Account+" "+ change_Password+" "+ change_Email);
            FutureTask<String> updateTask = new FutureTask<>(new NetworkThread(change_Name,change_Account, change_Password, change_Email, NetworkSettings.UPDATE_INFO));
            Thread thread = new Thread(updateTask);
            thread.start();
            try {
                ResponseBody body = new ObjectMapper().readValue(updateTask.get(), ResponseBody.class);
                String result = "";
                switch (body.getCode()) {
                    case 200:
                        result = "更新成功";
                        SharedPreferences.Editor note = getSharedPreferences("AccountInfo", Context.MODE_PRIVATE).edit();
                        note.putString("name",change_Name);
                        note.putString("account",change_Account);
                        note.putString("password",change_Password);
                        note.putString("email",change_Email);
                        note.commit();
                        break;
                    case 404:
                        result = "更新失败";
                        break;
                }
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                if(result.equals("更新成功")){
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}