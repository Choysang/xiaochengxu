package com.example.androiddingjing.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androiddingjing.R;

public class ClassIdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_id);

        Button teacher_button = (Button) findViewById(R.id.teacher_button);
        Button student_button = (Button) findViewById(R.id.student_button);

        teacher_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoTeacher();
            }
        });

        student_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoStudent();
            }
        });
    }

    private void gotoTeacher()
    {
        Intent teacherActivity = new Intent(this, TeacherIdActivity.class);
        startActivity(teacherActivity);
    }

    private void gotoStudent()
    {
        Intent studentActivity = new Intent(this, StudentIdActivity.class);
        startActivity(studentActivity);
    }
}