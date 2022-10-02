package com.example.androiddingjing.activities.ui.SignUp;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androiddingjing.R;
import com.example.androiddingjing.mysql.NetworkSettings;
import com.example.androiddingjing.mysql.NetworkThread;
import com.example.androiddingjing.mysql.ResponseBody;
import com.example.androiddingjing.tools.sendEmail;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.FutureTask;

public class SignUpFragment extends Fragment {

    private int nowIdCode;
    private SignUpViewModel mViewModel;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        EditText name =  getActivity().findViewById(R.id.Name);//输入的用户昵称
        EditText email =  getActivity().findViewById(R.id.Email);
        EditText idCode =  getActivity().findViewById(R.id.IdCode);//输入的验证码
        EditText account =  getActivity().findViewById(R.id.account);//输入的登录账号
        EditText password =  getActivity().findViewById(R.id.password);//输入的密码
        EditText rePassword =  getActivity().findViewById(R.id.rePassword);//确认的密码
        Button btn1 = getActivity().findViewById(R.id.btnGetIdCode);

        Button btnSignUp = getActivity().findViewById(R.id.btnRegister);
        btn1.setOnClickListener(v -> {
            nowIdCode = (int) Math.round(Math.random() * (9999-1000) +1000);
            String result = sendEmail.send(email.getText().toString(),"定睛APP注册验证码","您的验证码是："+nowIdCode);
            if(result.equals("邮件不合法")){
                Toast.makeText(getActivity(), "您的邮箱格式错误，请检查！", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(getActivity(), "验证码已发送，请查看邮箱", Toast.LENGTH_LONG).show();
            CountDownTimer mTimer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.d("countDownTime","millisUntilFinished: " + millisUntilFinished);
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

        btnSignUp.setOnClickListener(v -> {
            if(name.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "输入的用户名不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if(email.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "输入的电子邮箱不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if(!idCode.getText().toString().equals(String.valueOf(nowIdCode))){
                Toast.makeText(getActivity(), "验证码错误", Toast.LENGTH_LONG).show();
                return;
            }
            if(account.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "输入的账号不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if(password.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "输入的密码不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if(rePassword.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "输入的二次确认密码不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if(!rePassword.getText().toString().equals(password.getText().toString())){
                Toast.makeText(getActivity(), "输入的两个密码不一致", Toast.LENGTH_LONG).show();
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            FutureTask<String> signUpTask = new FutureTask<>(new NetworkThread(name.getText().toString(), account.getText().toString(), password.getText().toString(), email.getText().toString(), NetworkSettings.SIGN_UP));
            Thread thread = new Thread(signUpTask);
            thread.start();
            try {
                ResponseBody body = mapper.readValue(signUpTask.get(), ResponseBody.class);
                String result = "";
                switch (body.getCode()) {
                    case 200:
                        result = "注册成功";
                        getActivity().findViewById(R.id.btnLogin).callOnClick();//模拟点击登录页面，注册成功就切换到登录
                        break;
                    case 500:
                        result = "注册失败";
                        break;
                    case 501:
                        result = "该账号或邮箱已注册！";
                        break;
                }
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        // TODO: Use the ViewModel
    }



}