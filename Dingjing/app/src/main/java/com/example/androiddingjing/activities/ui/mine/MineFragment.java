package com.example.androiddingjing.activities.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.androiddingjing.R;
import com.example.androiddingjing.activities.ChangeInfoActivity;
import com.example.androiddingjing.activities.LoginAndRegisterActivity;
import com.example.androiddingjing.activities.QueryInfoActivity;

public class MineFragment extends Fragment {

    private MineViewModel mineViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mineViewModel =
                new ViewModelProvider(this).get(MineViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CardView changeInfoCard = getActivity().findViewById(R.id.changeInfo);
        changeInfoCard.setOnClickListener(v -> {
            Intent intent= new Intent(getActivity(), ChangeInfoActivity.class);
            startActivity(intent);
        });

        CardView queryInfoCard = getActivity().findViewById(R.id.queryInfo);
        queryInfoCard.setOnClickListener(v -> {
            Intent intent= new Intent(getActivity(), QueryInfoActivity.class);
            startActivity(intent);
        });

        CardView exitCard = getActivity().findViewById(R.id.exit);
        exitCard.setOnClickListener(v -> {
            getActivity().finish();
            Intent intent= new Intent(getActivity(), LoginAndRegisterActivity.class);
            startActivity(intent);
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences read = getActivity().getSharedPreferences("AccountInfo", Context.MODE_PRIVATE);
        TextView  nickname=getActivity().findViewById(R.id.nickname);
        nickname.setText(read.getString("name", null));
    }


}