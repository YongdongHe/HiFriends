package club.hifriends.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import club.hifriends.BaseAppCompatActivity;
import club.hifriends.BaseBlankActivity;
import okhttp3.Call;

/**
 * Created by heyon on 2016/1/29.
 */
public class AuthHelper {
    private Context context;
    private Activity activity;
//    public static String url = "http://139.129.23.136:8000/";
    public static String url = "http://192.168.1.107:8000/";//测试用本地服务
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public AuthHelper(final Activity activity){
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.pref = activity.getSharedPreferences("hifriends", Context.MODE_PRIVATE);
        this.editor = activity.getSharedPreferences("hifriends", Context.MODE_PRIVATE).edit();
    }

    public void checkAuth(){
        //检查uuid的正确情况，如果正确则更新个人信息
        String uuid = getUUID();
        if(uuid == ""){
            Toast.makeText(context, "请登录", Toast.LENGTH_SHORT);
            doLogout();
            return;
        }
        OkHttpUtils
                .post()
                .url(AuthHelper.url+"user/info")
                .addParams("uuid",uuid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        //错误检测
                        if (e instanceof SocketTimeoutException) {
                            Toast.makeText(context, "网络连接超时", Toast.LENGTH_SHORT);
                        } else if (e instanceof ConnectException) {
                            Toast.makeText(context, "网络连接错误", Toast.LENGTH_SHORT);
                        } else {
                            Toast.makeText(context, "\"未知错误\"", Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json_res = new JSONObject(response);
                            if (json_res.getInt("code") == 200) {
                                ////如果返回的状态码是200则说明uuid正确，则更新各类个人信息
                                setAuthCache("name", json_res.getString("name"));
                                setAuthCache("phone", json_res.getString("phone"));
                            } else {
                                //如果返回的状态码不是200则说明uuid不对，需要重新授权,则注销当前登录
                                Toast.makeText(context, "登录信息已失效，请重新登录", Toast.LENGTH_SHORT);
                                doLogout();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }




    public void doLogout() throws ClassCastException{
        //清除信息
        setAuthCache("uuid", "");
        setAuthCache("name", "");
        setAuthCache("phone", "");
        //跳转到登录页
        Intent intent = new Intent(context,LoginActivity.class);
        if(activity instanceof BaseAppCompatActivity){
            ((BaseAppCompatActivity) activity).startActivityAndFinish(intent);
        }else if (activity instanceof BaseBlankActivity) {
            ((BaseBlankActivity) activity).startActivityAndFinish(intent);
        }else{
            throw new ClassCastException();
        }
    }


    public String getUUID(){
        //获得存储的uuid
        String uuid = pref.getString("uuid","");
        return uuid;
    }

    public String getAuthCache(String cacheName){
        //可用
        /**
         * uuid         认证用uuid
         * cardnuim     一卡通号
         * schoolnum    学号
         * name         名字
         * sex          性别
         */
        //获得存储的某项信息
        String authCache = pref.getString(cacheName,"");
        return authCache;
    }

    public boolean setAuthCache(String cacheName,String cacheValue){
        //用于更新存储的某项信息
        editor.putString(cacheName, cacheValue);
        return editor.commit();
    }

}

