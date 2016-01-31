package club.hifriends.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import club.hifriends.BaseBlankActivity;
import club.hifriends.MainActivity;
import club.hifriends.R;
import okhttp3.Call;


/**
 * Created by heyon on 2016/1/28.
 */
public class LoginActivity extends BaseBlankActivity {
    EditText et_phone;
    EditText et_psd;
    Button btn_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        et_phone = (EditText)findViewById(R.id.et_login_phone);
        et_psd = (EditText)findViewById(R.id.et_login_psd);
        btn_log = (Button)findViewById(R.id.btn_login_login);

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
    }

    private void doLogin(){
        String phone = et_phone.getText().toString();
        String psd = et_psd.getText().toString();
        OkHttpUtils
                .post()
                .url(AuthHelper.url+"auth/dolog")
                .addParams("phone",phone)
                .addParams("psd",psd)
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
                                         String uuid = json_res.getString("uuid");
                                         getAuthHepler().setAuthCache("uuid", uuid);
                                         startActivity(new Intent(getBaseContext(), MainActivity.class));
                                         finish();
                                     }
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                     showMsg("数据解析错误");
                                 }
                             }
                         }
                );
    }

}
