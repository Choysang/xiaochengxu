package com.dingjing.news;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dingjing.news.gson.News;
import com.dingjing.news.gson.NewsList;
import com.dingjing.news.util.HttpUtil;
import com.dingjing.news.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final int  NEWS_DAILY= 1;
    private static final int  NEWS_SCIENCE= 2;
    private static final int  NEWS_EXPLORE= 3;
    private static final int  NEWS_AI= 4;
    private static final int  NEWS_BLOCKCHAIN= 5;
    private static final int  NEWS_IT= 6;
    private static final int  NEWS_VR= 7;
    private static final int  NEWS_MOBILE= 8;
    private static final int  NEWS_CREATE= 9;



    private final List<Title> titleList = new ArrayList<Title>();
    private ListView listView;
    private TitleAdapter adapter;
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("????????????");

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        listView = (ListView)findViewById(R.id.list_view);
        adapter = new TitleAdapter(this,R.layout.list_view_item, titleList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            final Intent intent = new Intent(MainActivity.this, ContentActivity.class);
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Title title = titleList.get(position);
                intent.putExtra("title",actionBar.getTitle());
                intent.putExtra("uri",title.getUri());
                startActivity(intent);
            }
        });

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.news_daily);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.news_daily:
                        handleCurrentPage("????????????",NEWS_DAILY);
                        break;
                    case R.id.news_science:
                        handleCurrentPage("????????????",NEWS_SCIENCE);
                        break;
                    case R.id.news_explore:
                        handleCurrentPage("????????????",NEWS_EXPLORE);
                        break;
                    case R.id.news_ai:
                        handleCurrentPage("??????????????????",NEWS_AI);
                        break;
                    case R.id.news_blockchain:
                        handleCurrentPage("???????????????",NEWS_BLOCKCHAIN);
                        break;
                    case R.id.news_it:
                        handleCurrentPage("IT??????",NEWS_IT);
                        break;
                    case R.id.news_vr:
                        handleCurrentPage("VR??????",NEWS_VR);
                        break;
                    case R.id.news_mobile:
                        handleCurrentPage("????????????",NEWS_MOBILE);
                        break;
                    case R.id.news_create:
                        handleCurrentPage("????????????",NEWS_CREATE);
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                int itemName = parseString((String) Objects.requireNonNull(actionBar.getTitle()));
                requestNew(itemName);
            }
        });

        requestNew(NEWS_DAILY);

    }

    /**
     *  ???????????????????????????,??????????????? ??????????????????
     */
    private void handleCurrentPage(String text, int item){
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        if (!text.equals(Objects.requireNonNull(actionBar.getTitle()).toString())){
            actionBar.setTitle(text);
            requestNew(item);
            refreshLayout.setRefreshing(true);
        }
    }


    /**
     * ??????????????????
     */
    public void requestNew(int itemName){

        // ?????????????????? URL ?????????????????????????????????
        String address = response(itemName);    // key
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = Objects.requireNonNull(response.body()).string();
                final NewsList newlist = Utility.parseJsonWithGson(responseText);
                final int code = newlist.code;
                final String msg = newlist.msg;
                if (code == 200){
                    titleList.clear();
                    for (News news:newlist.newsList){
                        Title title = new Title(news.title,news.description,news.picUrl, news.url);
                        titleList.add(title);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            listView.setSelection(0);
                            refreshLayout.setRefreshing(false);
                        };
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "??????????????????",Toast.LENGTH_SHORT).show();
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }



            }
        });


    }

    /**
     * ????????????????????????????????????????????? URL ??????
     */
    private String response(int itemName){
        String address = "https://api.tianapi.com/guonei/index?key=2799e0c2ec9d5305ba57e660057ed367&num=10";
        switch(itemName){
            case NEWS_DAILY:
                break;

            case NEWS_SCIENCE:
                address = address.replaceAll("guonei","keji");
                break;

            case NEWS_EXPLORE:
                address = address.replaceAll("guonei","sicprobe");
                break;

            case NEWS_AI:
                address = address.replaceAll("guonei","ai");
                break;

            case NEWS_BLOCKCHAIN:
                address = address.replaceAll("guonei","blockchain");
                break;

            case NEWS_IT:
                address = address.replaceAll("guonei","it");
                break;

            case NEWS_VR:
                address = address.replaceAll("guonei","vr");
                break;

            case NEWS_MOBILE:
                address = address.replaceAll("guonei","mobile");
                break;

            case NEWS_CREATE:
                address = address.replaceAll("guonei","startup");
                break;

            default:
        }
        return address;
    }

    /**
     * ?????? actionbar.getTitle() ??????????????????????????? ItemName
     */
    private int parseString(String text){
        if (text.equals("????????????")){
            return NEWS_DAILY;
        }
        if (text.equals("????????????")){
            return NEWS_SCIENCE;
        }
        if (text.equals("????????????")){
            return NEWS_EXPLORE;
        }
        if (text.equals("??????????????????")){
            return NEWS_AI;
        }
        if (text.equals("???????????????")){
            return NEWS_BLOCKCHAIN;
        }
        if (text.equals("IT??????")){
            return NEWS_IT;
        }
        if (text.equals("VR??????")){
            return NEWS_VR;
        }
        if (text.equals("??????????????????")){
            return NEWS_MOBILE;
        }
        if (text.equals("????????????")){
            return NEWS_CREATE;
        }
        return NEWS_DAILY;
    }

    /**
     * ????????????????????????????????????????????????
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????? activity
     */
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else{
            finish();
        }
    }
}
