package com.example.androiddingjing.mysql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SmallStudyDataBase extends SQLiteOpenHelper {

    public static final String CREATE_SMALL_STUDY_TABLE = "create table SmallStudydb (" +
            "id integer primary key autoincrement," +
            "snumber text," +
            "sname text," +
            "class text," +
            "tnumber text," +
            "concentration text)";

    private final Context mcontext;

    public SmallStudyDataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SMALL_STUDY_TABLE);
        Toast.makeText(mcontext, "创建班级成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

