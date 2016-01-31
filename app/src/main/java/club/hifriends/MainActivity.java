package club.hifriends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import club.hifriends.activity.ActivityHelper;
import club.hifriends.activity.ActivityItem;
import club.hifriends.activity.ActivityListAdapter;
import club.hifriends.auth.AuthException;
import club.hifriends.auth.AuthHelper;
import club.hifriends.setting.SettingActivity;
import okhttp3.Call;

public class MainActivity extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    SwipeRefreshLayout swipeRefreshLayout;
    NavigationView navigationView;
    ListView lt_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lt_activity = (ListView)findViewById(R.id.listview_activity_list);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipecontainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshActivityList();
            }
        });



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onRefreshActivityList();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getBaseContext(), SettingActivity.class));
        }else if(id == R.id.action_logout){
            AlertDialog.Builder confirmDialog =new AlertDialog.Builder(this);
            confirmDialog.setTitle("确认");
            confirmDialog.setMessage("确定要退出登录吗？");
            confirmDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getAuthHepler().doLogout();
                }
            });
            confirmDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showMsg("请继续享受与朋友们相处的时光吧~");
                }
            });
            confirmDialog.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onRefreshActivityList(){
        OkHttpUtils
                .get()
                .url(AuthHelper.url+"activity/list")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        if (e instanceof SocketTimeoutException) {
                            showMsg("网路连接超时");
                        } else if (e instanceof ConnectException) {
                            showMsg("网络连接错误");
                        } else {
                            showMsg("未知错误");
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json_res = new JSONObject(response);
                            if (json_res.getInt("code") == 200) {
                                showMsg("刷新成功");
                                JSONArray activiesArray = json_res.getJSONArray("activities");
                                //将json数组转化为adapter可用的activityArrayList
                                ArrayList<ActivityItem> activityItemArrayList =
                                        ActivityHelper.transfromJSONtoArrayList(activiesArray);
                                //构造listview的adapter
                                ActivityListAdapter activityListAdapter =
                                        new ActivityListAdapter(MainActivity.this, R.layout.listview_activity_item, activityItemArrayList);
                                lt_activity.setAdapter(activityListAdapter);
                                Toast.makeText(getApplicationContext(),"刷新成功",Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            } else {
                                showMsg("服务器发生了未知错误，请稍后再试。");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showMsg("数据解析错误");
                        }
                    }
                });


    }
}
