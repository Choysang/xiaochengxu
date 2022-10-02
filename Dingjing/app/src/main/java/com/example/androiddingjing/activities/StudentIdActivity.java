package com.example.androiddingjing.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddingjing.R;

import java.util.Vector;

public class StudentIdActivity extends AppCompatActivity {

    private final Vector<Button> classes = new Vector<Button>();

    private void addClassView(LinearLayout LL, String className){
        //include Class Name and Manage
        LinearLayout new_class = new LinearLayout(StudentIdActivity.this);
        new_class.setOrientation(LinearLayout.VERTICAL);
        new_class.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
        LinearLayout.LayoutParams class_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        class_params.setMargins(0, 60, 0, 0);
        new_class.setLayoutParams(class_params);  //设置参数
        new_class.setPadding(30, 10, 30, 10);

        //Class Name
        TextView cn = new TextView(StudentIdActivity.this);
        LinearLayout.LayoutParams cn_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200,2.0f);
        cn.setLayoutParams(cn_params);
        cn.setTextSize(50);
        cn.setGravity(Gravity.CENTER_VERTICAL);
        cn.setText(className);

        //Manage Text
        Button manage = new Button(StudentIdActivity.this);
        LinearLayout.LayoutParams manage_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150,1.0f);
        manage.setLayoutParams(manage_params);
        manage.setTextSize(25);
        manage.setGravity(Gravity.CENTER);
        manage.setBackgroundColor(getResources().getColor(R.color.white));
        manage.setTextColor(getResources().getColor(R.color.blue));
        manage.setText("加入");
        classes.addElement(manage);

        //add View
        new_class.addView(cn);
        new_class.addView(manage);
        new_class.setBackground(getResources().getDrawable(R.drawable.underline));
        LL.addView(new_class);
    }

    private void initialClass(LinearLayout LL){
        String[] initialClasses = {"1班", "2班", "3班"};
        for(int i=0;i<3;i++)
        {
            addClassView(LL, initialClasses[i]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_id);

        LinearLayout classLL = (LinearLayout) findViewById(R.id.MyClasses);
        initialClass(classLL);

        for(int i=0;i<classes.size();i++)
        {
            Button mng = classes.get(i);
            mng.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(StudentIdActivity.this,"成功加入",Toast.LENGTH_SHORT).show();
                    mng.setText("已加入");
                    mng.setTextColor(getResources().getColor(R.color.gainsboro));
                }
            });
        }
    }
}