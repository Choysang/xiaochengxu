package com.example.androiddingjing.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.androiddingjing.R;

import java.util.Vector;

public class ManageStudent extends AppCompatActivity {
    private final Vector<String> studentsName = new Vector<String>();

    private final Vector<Button> deleteStudent = new Vector<Button>();

    private String studentName = "";

    //初始化
    private void initialStudentName(LinearLayout LL){
        studentsName.addElement("张三");
        studentsName.addElement("李四");
        studentsName.addElement("王五");
        for(int i=0;i<studentsName.size();i++)
        {
            addStudent(LL, studentsName.get(i));
        }
    }

    //确认框
    private void confirmTitleDialog(LinearLayout LL, LinearLayout si){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete_title)).setNegativeButton(
                getString(R.string.dialog_cancel), null);
        builder.setPositiveButton(getString(R.string.dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LL.removeView(si);
                    }
                });
        builder.show();
    }

    //修改信息
    private void changeTitleDialog(TextView tv) {

        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.input_student_name)).setView(inputServer).setNegativeButton(
                getString(R.string.dialog_cancel), null);
        builder.setPositiveButton(getString(R.string.dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        studentName = inputServer.getText().toString();
                        ScrollView classSV = (ScrollView) findViewById(R.id.MyStudent);
                        LinearLayout classLL = (LinearLayout) findViewById(R.id.MyStudents);
                        if(!studentName.equals(""))
                        {
                            tv.setText(studentName);
                            studentName="";
                        }
                        classSV.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
        builder.show();
    }

    private void addStudent(LinearLayout LL, String studentName){
        //include student and divide line
        LinearLayout studentBlock = new LinearLayout(this);
        LinearLayout.LayoutParams studentLL_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        studentBlock.setLayoutParams(studentLL_params);
        studentBlock.setOrientation(LinearLayout.VERTICAL);

        //include student information and control button
        LinearLayout student = new LinearLayout(ManageStudent.this);
        student.setLayoutParams(studentLL_params);
        student.setPadding(0, 30, 0, 0);
        student.setOrientation(LinearLayout.HORIZONTAL);

        //Build Student Information
        TextView si = new TextView(ManageStudent.this);
        LinearLayout.LayoutParams si_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 8);
        si.setLayoutParams(si_params);
        si.setTextSize(25);
        si.setGravity(Gravity.CENTER_VERTICAL);
        si.setText(studentName);
        si.setTextColor(getResources().getColor(R.color.black));
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTitleDialog(si);
            }
        });

        //Build Delete Icon
        TextView deleteText = new TextView(ManageStudent.this);
        LinearLayout.LayoutParams db_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,2);
        deleteText.setLayoutParams(db_params);
        deleteText.setText("删除");
        deleteText.setTextSize(15);
        deleteText.setGravity(Gravity.RIGHT);
        deleteText.setTextColor(getResources().getColor(R.color.Crimson));
        deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmTitleDialog(LL, studentBlock);
            }
        });

        //Horizon Divide Line
        LinearLayout horizon_divideLine = new LinearLayout(this);
        LinearLayout.LayoutParams horizonDivideLine_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
        horizon_divideLine.setLayoutParams(horizonDivideLine_params);
        horizon_divideLine.setBackgroundColor(getResources().getColor(R.color.gainsboro));

        //Build Student LinearLayout
        student.addView(si);
        student.addView(deleteText);

        //Build Student Block
        studentBlock.addView(student);
        studentBlock.addView(horizon_divideLine);

        //add to ScrollView's LinearLayout
        LL.addView(studentBlock);
    }

    //输入框信息
    private void inputTitleDialog() {

        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.input_student_name)).setView(inputServer).setNegativeButton(
                getString(R.string.dialog_cancel), null);
        builder.setPositiveButton(getString(R.string.dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        studentName = inputServer.getText().toString();
                        ScrollView classSV = (ScrollView) findViewById(R.id.MyStudent);
                        LinearLayout classLL = (LinearLayout) findViewById(R.id.MyStudents);
                        if(!studentName.equals(""))
                        {
                            addStudent(classLL, studentName);
                            studentName="";
                        }
                        classSV.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_student);

        LinearLayout myStudents = (LinearLayout) findViewById(R.id.MyStudents);
        initialStudentName(myStudents);

        TextView addStudent = (TextView) findViewById(R.id.create_button);
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputTitleDialog();
            }
        });
    }
}