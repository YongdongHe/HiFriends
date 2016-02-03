package club.hifriends.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;


import club.hifriends.BaseAppCompatActivity;
import club.hifriends.R;
import club.hifriends.activity.adapter.UserListAdapter;
import club.hifriends.activity.beans.ActivityInfo;
import club.hifriends.auth.AuthHelper;
import okhttp3.Call;

public class LookInfoActivity extends BaseAppCompatActivity {

    //页面的activity id
    String activityId;

    //布局控件
    TextView tv_title;
    TextView tv_content;
    TextView tv_time;
    TextView tv_leader;

    ListView liv_partner;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_info);
        this.activityId = getIntent().getExtras().getString("activity_id");

        //设置toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_24dp_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle("活动详情");

        //控件初始化
        tv_title = (TextView)findViewById(R.id.tv_activity_info_title);
        tv_time = (TextView)findViewById(R.id.tv_activity_info_time);
        tv_content = (TextView)findViewById(R.id.tv_activity_info_content);
        tv_leader = (TextView) findViewById(R.id.tv_activity_info_leader);

        liv_partner = (ListView) findViewById(R.id.listview_activity_info_partnerlist);


        //点击浮动的按钮则加入当前活动
        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab_activity_info_join);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join();
            }
        });
        refresh();

    }

    public void refresh(){
        OkHttpUtils
                .get()
                .url(AuthHelper.url+"activity/status")
                .addParams("activity_id", this.activityId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        if (e instanceof SocketTimeoutException) {
                            Toast.makeText(getBaseContext(),"网络连接超时",Toast.LENGTH_SHORT).show();
                        } else if (e instanceof ConnectException) {
                            Toast.makeText(getBaseContext(),"网络连接错误",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getBaseContext(),"未知错误",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json_res = new JSONObject(response);
                            if (json_res.getInt("code") == 200) {
                                ActivityInfo activityInfo = new ActivityInfo(json_res.getJSONObject("content"));
                                tv_leader.setText(activityInfo.getLeader());
                                tv_title.setText(activityInfo.getTitle());
                                tv_time.setText(activityInfo.getTime());
                                tv_content.setText(activityInfo.getContent());
                                liv_partner.setAdapter(new UserListAdapter(getBaseContext(),
                                        R.layout.listview_activity_info_item, activityInfo.getPartnerArrayList()));

                                //根据listview的item数目和高度设置本身的高度，以保证listview能全部展开展示
                                UserListAdapter.setListViewHeightBasedOnChildren(liv_partner);
                            } else if (json_res.getInt("code") == 404) {
                                Toast.makeText(getBaseContext(),"该活动不存在或者已被删除",Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(getBaseContext(),json_res.getString("content"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(),"数据解析错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void join(){
        OkHttpUtils
                .post()
                .url(AuthHelper.url + "activity/join")
                .addParams("activity_id", activityId)
                .addParams("uuid",getAuthHepler().getUUID())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        if (e instanceof SocketTimeoutException) {
                            Toast.makeText(getBaseContext(),"网络连接超时",Toast.LENGTH_SHORT).show();
                        } else if (e instanceof ConnectException) {
                            Toast.makeText(getBaseContext(),"网络连接错误",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getBaseContext(),"未知错误",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json_res = new JSONObject(response);
                            int code = json_res.getInt("code");
                            if (code == 200) {
                                //如果为200则表示加入活动成功，刷新当前页面

                                refresh();
                            }else if(code == 401){
                                getAuthHepler().doLogout();
                            }
                            showMsg(json_res.getString("content"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(),"数据解析错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void call(String phoneNumber)throws SecurityException {
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
    }

}
