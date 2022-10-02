package com.example.androiddingjing.activities.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androiddingjing.R;
import com.example.androiddingjing.activities.MainActivity;
import com.example.androiddingjing.mysql.NetworkSettings;
import com.example.androiddingjing.mysql.NetworkThread;
import com.example.androiddingjing.mysql.ResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.concurrent.FutureTask;



public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private final ObjectMapper mapper = new ObjectMapper();

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        EditText account =  getActivity().findViewById(R.id.account);//输入的登录账号
        EditText password =  getActivity().findViewById(R.id.password);//输入的密码
        Button btn1 = getActivity().findViewById(R.id.btnLogin1);

        btn1.setOnClickListener(v -> {
            if(account.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "输入的账号不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if(password.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "输入的密码不能为空", Toast.LENGTH_LONG).show();
                return;
            }

            FutureTask<String> signInTask = new FutureTask<>(new NetworkThread("",account.getText().toString(), password.getText().toString(), "",NetworkSettings.SIGN_IN));
            Thread thread = new Thread(signInTask);
            thread.start();
            try {
                ResponseBody body = mapper.readValue(signInTask.get(), ResponseBody.class);

                Toast.makeText(getActivity(), body.getCode() == 200 ? "登录成功" : "登录失败", Toast.LENGTH_SHORT).show();
                if (body.getCode() == 200) {//登录成功
                    /**
                     * 从服务器获取当前账号的信息并保存到本地
                     * 本地信息读取方式：
                     * SharedPreferences read = this.getSharedPreferences("AccountInfo", Context.MODE_PRIVATE);
                     * read.getString("account", null);
                     * */
                    //从服务器获取此账号的信息
                    FutureTask<String> searchInfoTask = new FutureTask<>(new NetworkThread("",account.getText().toString(), "", "",NetworkSettings.SEARCH_INFO));
                    new Thread(searchInfoTask).start();
                    ResponseBody body1 = new ObjectMapper().readValue(searchInfoTask.get(), ResponseBody.class);
                    JSONObject userInfo = new JSONObject(body1.getData().toString());
                    //将获取的信息保存到本地
                    SharedPreferences.Editor note = getActivity().getSharedPreferences("AccountInfo", Context.MODE_PRIVATE).edit();
                    note.putString("name",userInfo.getString("name"));
                    note.putString("account",userInfo.getString("account"));
                    note.putString("password",userInfo.getString("password"));
                    note.putString("email",userInfo.getString("email"));
                    note.commit();

                    getActivity().finish();
                    //跳转到主界面
                    Intent intent= new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        // TODO: Use the ViewModel
    }



}