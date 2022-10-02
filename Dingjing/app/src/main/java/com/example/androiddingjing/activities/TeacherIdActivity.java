package com.example.androiddingjing.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.androiddingjing.R;
import com.example.androiddingjing.mysql.SmallStudyDataBase;

import java.util.Vector;

public class TeacherIdActivity extends AppCompatActivity {

    private SmallStudyDataBase db;

    private final Vector<Button> classes = new Vector<Button>();

    private String className = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_id);

        LinearLayout classLL = (LinearLayout) findViewById(R.id.MyClasses);
        initialClass(classLL);

        db = new SmallStudyDataBase(this, "SmallStudy.db", null, 1);

        TextView create_button = (TextView) findViewById(R.id.create_button);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.getWritableDatabase();
                inputTitleDialog();
            }
        });
    }

    private void goToManage(){
        Intent manageStudent = new Intent(this, ManageStudent.class);
        startActivity(manageStudent);
    }

    private void addClassView(LinearLayout LL, String className){
        //include Class Name and Manage
        LinearLayout new_class = new LinearLayout(TeacherIdActivity.this);
        new_class.setOrientation(LinearLayout.VERTICAL);
        new_class.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
        LinearLayout.LayoutParams class_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        class_params.setMargins(0, 60, 0, 0);
        new_class.setLayoutParams(class_params);  //设置参数
        new_class.setPadding(30, 10, 30, 10);

        //Class Name
        TextView cn = new TextView(TeacherIdActivity.this);
        LinearLayout.LayoutParams cn_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200,2.0f);
        cn.setLayoutParams(cn_params);
        cn.setTextSize(50);
        cn.setGravity(Gravity.CENTER_VERTICAL);
        cn.setText(className);

        //Horizon Divide Line
        LinearLayout horizon_divideLine = new LinearLayout(TeacherIdActivity.this);
        LinearLayout.LayoutParams horizonDivideLine_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
        horizon_divideLine.setLayoutParams(horizonDivideLine_params);
        horizon_divideLine.setBackgroundColor(getResources().getColor(R.color.gainsboro));

        LinearLayout control = new LinearLayout(TeacherIdActivity.this);
        control.setOrientation(LinearLayout.HORIZONTAL);

        //Manage Button
        Button manage = new Button(TeacherIdActivity.this);
        LinearLayout.LayoutParams manage_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150,1.0f);
        manage.setLayoutParams(manage_params);
        manage.setTextSize(25);
        manage.setGravity(Gravity.CENTER);
        manage.setBackgroundColor(getResources().getColor(R.color.white));
        manage.setTextColor(getResources().getColor(R.color.blue));
        manage.setText("管理");
        classes.addElement(manage);
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToManage();
            }
        });

        //Divide Line
        LinearLayout divideLine = new LinearLayout(TeacherIdActivity.this);
        LinearLayout.LayoutParams divideLine_params = new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.MATCH_PARENT);
        divideLine.setLayoutParams(divideLine_params);
        divideLine.setBackgroundColor(getResources().getColor(R.color.gainsboro));

        //Report Button
        Button report = new Button(TeacherIdActivity.this);
        report.setLayoutParams(manage_params);
        report.setTextSize(25);
        report.setGravity(Gravity.CENTER);
        report.setBackgroundColor(getResources().getColor(R.color.white));
        report.setTextColor(getResources().getColor(R.color.blue));
        report.setText("生成报告");

        //build control LinearLayout
        control.addView(manage);
        control.addView(divideLine);
        control.addView(report);

        //build New Class LinearLayout
        new_class.addView(cn);
        new_class.addView(horizon_divideLine);
        new_class.addView(control);
        new_class.setBackground(getResources().getDrawable(R.drawable.underline));

        //add to ScrollView's LinearLayout
        LL.addView(new_class);
    }

    private void initialClass(LinearLayout LL){
        String[] initialClasses = {"1班", "2班", "3班"};
        for(int i=0;i<3;i++)
        {
            addClassView(LL, initialClasses[i]);
        }
    }

    private void inputTitleDialog() {

        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.input_class_name)).setView(inputServer).setNegativeButton(
                getString(R.string.dialog_cancel), null);
        builder.setPositiveButton(getString(R.string.dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        className = inputServer.getText().toString();
                        ScrollView classSV = (ScrollView) findViewById(R.id.MyClass);
                        LinearLayout classLL = (LinearLayout) findViewById(R.id.MyClasses);
                        if(!className.equals(""))
                        {
                            addClassView(classLL, className);
                            className="";
                        }
                        classSV.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
        builder.show();
    }
}