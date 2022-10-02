package com.example.androiddingjing.activities.ui.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.androiddingjing.R;
import com.example.androiddingjing.activities.CameraActivity;
import com.example.androiddingjing.activities.RecordActivity;

public class TestFragment extends Fragment {

    private TestViewModel testViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        testViewModel =
                new ViewModelProvider(this).get(TestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_test, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CardView onlineTest = getActivity().findViewById(R.id.onlineTestCard);
        CardView localTest = getActivity().findViewById(R.id.localTestCard);
        CardView history = getActivity().findViewById(R.id.historyCard);



        onlineTest.setOnClickListener(v -> {
                Toast.makeText(getActivity(), "onlineTestCard Clicked!", Toast.LENGTH_SHORT).show();
        });

        localTest.setOnClickListener(v -> {

            Intent it = new Intent(this.getActivity(), CameraActivity.class);
            startActivity(it);
//            Toast.makeText(getActivity(), "localTestCard Clicked!", Toast.LENGTH_SHORT).show();
        });

        history.setOnClickListener(v -> {
            Intent it = new Intent(this.getActivity(), RecordActivity.class);
            startActivity(it);
//            Toast.makeText(getActivity(), "historyCard Clicked!", Toast.LENGTH_SHORT).show();
        });
    }


//    private void jumpToCameraActivity() {
//
//    }
}