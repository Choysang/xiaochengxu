package com.example.androiddingjing.activities.ui.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class  GroupViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GroupViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is news fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}