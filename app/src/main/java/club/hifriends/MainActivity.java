package club.hifriends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import club.hifriends.activity.ActivityHelper;
import club.hifriends.activity.PublishNewActivity;
import club.hifriends.activity.beans.ActivityItem;
import club.hifriends.activity.adapter.ActivityListAdapter;
import club.hifriends.auth.AuthHelper;
import club.hifriends.setting.InfoEditActivity;
import club.hifriends.setting.SettingActivity;
import okhttp3.Call;

public class MainActivity extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    SwipeRefreshLayout swipeRefreshLayout;
    NavigationView navigationView;
    TextView tv_name;
    TextView tv_phone;
    ListView lt_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar设置
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PublishNewActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //侧边栏布局加载和控件获取
        View navigationViewHeader = navigationView.inflateHeaderView(R.layout.nav_header_main);
        tv_name = (TextView)navigationViewHeader.findViewById(R.id.tv_name);
        tv_phone =(TextView)navigationViewHeader.findViewById(R.id.tv_phone);


        lt_activity = (ListView)findViewById(R.id.listview_activity_list);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipecontainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshActivityList();
            }
        });


        onRefreshActivityList();
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
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
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
        }else if(id == R.id.action_message){
            showMsg("获取消息");
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // 关闭侧边栏回到首页
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_star) {

        } else if (id == R.id.nav_settings) {
            //打开设置
            startActivity(new Intent(getBaseContext(), SettingActivity.class));
        } else if(id == R.id.nav_info){
            startActivity(new Intent(getBaseContext(), InfoEditActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_aboutus) {
            AlertDialog.Builder normalDia=new AlertDialog.Builder(this);
            normalDia.setTitle("关于我们");
            normalDia.setMessage("制作：he-real\n联系方式:609978993");
            normalDia.setPositiveButton("确定", null);
            normalDia.create().show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //刷新活动页
    public void onRefreshActivityList(){
        tv_name.setText(getAuthHepler().getAuthCache("name"));
        tv_phone.setText(getAuthHepler().getAuthCache("phone"));
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
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            JSONObject json_res = new JSONObject(response);
                            if (json_res.getInt("code") == 200) {
//                                showMsg("已获取最新活动。");
                                JSONArray activiesArray = json_res.getJSONArray("activities");
                                //将json数组转化为adapter可用的activityArrayList
                                ArrayList<ActivityItem> activityItemArrayList =
                                        ActivityHelper.transfromJSONtoArrayList(activiesArray);
                                //构造listview的adapter
                                ActivityListAdapter activityListAdapter =
                                        new ActivityListAdapter(MainActivity.this, R.layout.listview_activity_item, activityItemArrayList);
                                lt_activity.setAdapter(activityListAdapter);
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
