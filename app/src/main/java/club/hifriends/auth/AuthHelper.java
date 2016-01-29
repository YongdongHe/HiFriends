package club.hifriends.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.NetworkOnMainThreadException;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by heyon on 2016/1/29.
 */
public class AuthHelper {
    private Context context;
    public static String url = "http://139.129.23.136:8000/";
    public AuthHelper(Context context){
        this.context = context;
    }

    public void checkAuth()throws NetworkOnMainThreadException {
        //检查uuid的正确情况，如果正确则更新个人信息
        String uuid = getUUID();

    }

    public String doLogin(String phone,String psd)throws AuthExcepetion{
        //联网登录，获取并保存uuid,然后返回
        OkHttpUtils
                .post()
                .url(AuthHelper.url+"auth/dolog")
                .addParams("phone",phone)
                .addParams("psd",psd)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Object response) {

                    }
                });
        return null;
    }

    public void doLogout() {
        setAuthCache("uuid", "");
    }

    public String getUUID(){
        //获得存储的uuid
        SharedPreferences pref = context.getSharedPreferences("hifriends", Context.MODE_PRIVATE);
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
        SharedPreferences pref = context.getSharedPreferences("hifriends", Context.MODE_PRIVATE);
        String authCache = pref.getString(cacheName,"");
        return authCache;
    }

    public boolean setAuthCache(String cacheName,String cacheValue){
        //用于更新存储的某项信息
        SharedPreferences.Editor editor= context.getSharedPreferences("hifriends",context.MODE_PRIVATE).edit();
        editor.putString(cacheName, cacheValue);
        return editor.commit();
    }

}

class AuthExcepetion extends Exception{
    public AuthExcepetion(){

    }
}