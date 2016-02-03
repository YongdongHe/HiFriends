package club.hifriends.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import club.hifriends.R;
import okhttp3.Call;


/**
 * Created by heyon on 2016/1/28.
 */
public class RegisterActivity extends BaseBlankActivity {
    Button btn_getauth;
    Button btn_register;
    EditText et_phone;
    EditText et_authcode;
    EditText et_psd;
    Thread threadTimer;
    Handler handlerTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_register);

        btn_getauth = (Button)findViewById(R.id.btn_register_getauth);
        btn_register = (Button)findViewById(R.id.btn_register_register);
        et_phone = (EditText)findViewById(R.id.et_register_phone);
        et_authcode = (EditText)findViewById(R.id.et_register_auth);
        et_psd = (EditText)findViewById(R.id.et_register_psd);



        //处理获取验证码按钮的handler
        handlerTimer = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.obj instanceof InterruptedException)
                {
                    ((InterruptedException) msg.obj).printStackTrace();
                }else{
                    int second = (int)msg.obj;
                    btn_getauth.setText("重新获取("+second+")");
                    if(second == 0){
                        btn_getauth.setEnabled(true);
                    }
                }
                return false;
            }
        });


        //获取验证码按钮绑定响应事件
        btn_getauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
            }
        });

        //注册按钮绑定响应事件
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });
    }

    private void startTimer(){
        //用于控制重新获取验证码按钮，定时30秒倒计时线程
        threadTimer = new Thread(new Runnable() {
            @Override
            public void run() {
                int second = 30;

                try {
                    while(second>=0) {
                        Message msg = new Message();
                        Thread.sleep(1000);
                        msg.obj = second;
                        handlerTimer.sendMessage(msg);
                        second-=1;
                    }
                }catch (InterruptedException e) {
                    Message msg = new Message();
                    e.printStackTrace();
                    msg.obj = e;
                    handlerTimer.sendMessage(msg);
                }
            }
        });
        threadTimer.start();
        btn_getauth.setEnabled(false);
    }

    private void getCode(){
        //发送验证码获取请求
        Log.d("phone",et_phone.getText().toString());
        OkHttpUtils
                .post()
                .url(AuthHelper.url+"auth/getcode")
                        .addParams("phone", et_phone.getText().toString())
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
                                    if (json_res.getInt("code") == 200)
                                        startTimer();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    showMsg("数据解析错误");
                                }
                            }
                        });
    }

    private void doRegister(){
        //发送注册获取请求
        OkHttpUtils
                .post()
                .url(AuthHelper.url+"auth/doregister")
                        .addParams("phone", et_phone.getText().toString())
                        .addParams("captha", et_authcode.getText().toString())
                        .addParams("psd", et_psd.getText().toString())
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
                                        startActivity(new Intent(getBaseContext(), LoginActivity.class));
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    showMsg("数据解析错误");
                                }
                            }
                        });
    }



}
