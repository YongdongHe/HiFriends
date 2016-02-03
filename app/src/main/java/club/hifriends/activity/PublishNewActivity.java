package club.hifriends.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import club.hifriends.BaseAppCompatActivity;
import club.hifriends.R;
import club.hifriends.activity.adapter.ActivityListAdapter;
import club.hifriends.activity.beans.ActivityItem;
import club.hifriends.auth.AuthHelper;
import okhttp3.Call;

public class PublishNewActivity extends BaseAppCompatActivity {

    EditText tv_title;
    EditText tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_24dp);
        toolbar.setTitle("发布新活动");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_publish_new);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publish();
            }
        });

        //控件初始化
        tv_title= (EditText)findViewById(R.id.tv_publish_new_title);
        tv_content=(EditText)findViewById(R.id.tv_publish_new_content);
    }

    public void publish(){
        OkHttpUtils
                .post()
                .url(AuthHelper.url+"activity/publish")
                .addParams("uuid",getAuthHepler().getUUID())
                .addParams("title",tv_title.getText().toString())
                .addParams("content",tv_content.getText().toString())
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
                                showMsg(json_res.getString("content"));
                                onBackPressed();
                                finish();
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
