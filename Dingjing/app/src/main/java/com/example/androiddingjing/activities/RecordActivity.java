package com.example.androiddingjing.activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddingjing.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;

import androidx.camera.core.CameraXConfig;

import androidx.camera.camera2.Camera2Config;

class MyCameraXApplication extends Application implements CameraXConfig.Provider {
    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }
}

public class RecordActivity extends AppCompatActivity {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private PreviewView previewView;
    private ListView listView;
    private RecordAdapter adapter;
    private List<RecordItem> listRecord;
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_view);

        //
        dbHelper = new MyDBHelper(this, "test.sqlite", null, 1);
        db = dbHelper.getReadableDatabase();
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new RecordAdapter(this, new ArrayList<RecordItem>()));

        List<RecordItem> list = new ArrayList<>();


        // 手动插几条数据....
        list.add(new RecordItem("   检测结果：分心(专注度21%)","2021-8-21 10:57",114));
        list.add(new RecordItem("   检测结果：正常(专注度55%)","2021-8-21 10:59",514));
        list.add(new RecordItem("   检测结果：专注(专注度87%)","2021-8-21 11:06",515));
        list.add(new RecordItem("   检测结果：正常(专注度68%)","2021-8-21 11:07",516));

        listRecord = list;
        listView.setAdapter(new RecordAdapter(this, listRecord));


    }



    private void loadFromDatabase() {
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        try {
            Cursor c = db.rawQuery("SELECT * FROM detect_history;", null);
            c.moveToFirst();
            List<RecordItem> list = new ArrayList<>();
            while (!c.isAfterLast()) {
                RecordItem it = new RecordItem();
                it.setPath(c.getString(1));
                it.setDate(c.getString(2));
                list.add(it);
                c.moveToNext();
            }
            c.close();
            listRecord = list;
            listView.setAdapter(new RecordAdapter(this, listRecord));
        } catch (Exception ignore) {
            Log.e("?", ignore.toString());
        }
    }

    private void insertIntoDatabase() {
//                RecordItem r = new RecordItem();
//                /*
//                    Put detect function here.
//                 */
//                r.setPath("Hello");
//                r.setDate(",Hi");
//
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("video_file_path", r.getPath());
//                contentValues.put("date", r.getDate());
//                db.insert("detect_history", null, contentValues);
    }
}

class MyDBHelper extends SQLiteOpenHelper {
    private String create_sql = "CREATE TABLE IF NOT EXISTS detect_history  " +
            "(id integer PRIMARY KEY AUTOINCREMENT NOT NULL,video_file_path text(128),date timestamp(128) );";
    private static Integer Version = 1;

    public MyDBHelper(@Nullable Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(create_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}



class RecordItem {
    public RecordItem(String s,String p, int i) {
        setId(i);
        setPath(p);
        setDate(s);
    }
    public RecordItem() {
        setId(-1);
        setPath("");
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String path;
    private String date;

}


class RecordAdapter extends ArrayAdapter<RecordItem> {
    private List<RecordItem> listItem;

    public RecordAdapter(@NonNull Context context, List<RecordItem> objects) {
        super(context, 0, objects);
        listItem = objects;
    }

    public void fetchData(List<RecordItem> l) {
        listItem = l;
    }

    @Override
    public View getView(int position, View contextView, ViewGroup viewGroup) {
        contextView = LayoutInflater.from(getContext()).inflate(R.layout.record_item, null);

        RecordItem ri = listItem.get(position);
        TextView f = (TextView) contextView.findViewById(R.id.filePath);
        TextView t = (TextView) contextView.findViewById(R.id.date);
        f.setText(ri.getPath());
        t.setText(ri.getDate());
        return contextView;
    }
}
