package club.hifriends.setting;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;


import club.hifriends.BaseAppCompatActivity;
import club.hifriends.R;
import club.hifriends.auth.AuthHelper;
import okhttp3.Call;

/**
 * 编辑个人信息的activity
 * Created by heyon on 2016/2/3.
 */


public class InfoEditActivity extends BaseAppCompatActivity {
    EditText et_username;
    Button btn_changeicon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_24dp_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle("编辑个人信息");


        //控件初始化
        et_username = (EditText)findViewById(R.id.tv_user_name);

        //浮动按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_change);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeInfo();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info_edit, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ok) {
            //确认修改

        }
        return super.onOptionsItemSelected(item);
    }

    public void changeInfo(){
        OkHttpUtils
                .post()
                .url(AuthHelper.url+"user/info")
                .addParams("uuid",getAuthHepler().getUUID())
                .addParams("name",et_username.getText().toString())
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
                            showMsg(json_res.getString("content"));
                            if (json_res.getInt("code") == 200) {
                                //更新授权信息并且更新个人信息
                                getAuthHepler().checkAuth();
                                onBackPressed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showMsg("数据解析错误");
                        }
                    }
                });
    }

}