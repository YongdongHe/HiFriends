package club.hifriends.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import club.hifriends.BaseBlankActivity;
import club.hifriends.MainActivity;
import club.hifriends.R;

/**
 * Created by heyon on 2016/1/28.
 */
public class AuthActivity extends BaseBlankActivity {
    WelcomeFragment welcomeFragment;
    SelectFragment selectFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        welcomeFragment = new WelcomeFragment();
        getFragmentManager().beginTransaction().replace(R.id.activity_auth_container,welcomeFragment).commit();
        //延时结束欢迎界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishWelcome();
            }
        },500);
    }

    public boolean checkAuth(){
        //授权认证有效则输入
        SharedPreferences sharedPreferences = getSharedPreferences("hifriends",MODE_PRIVATE);
        String uuid = sharedPreferences.getString("uuid","");
        if (uuid.equals(""))
            return false;
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void finishWelcome(){
        if(checkAuth())
        {
            //如果uuid存在则获取用户信息并且打开主界面
            getAuthHepler().checkAuth();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();//释放当前的activity
        }else{
            Toast.makeText(AuthActivity.this, "请先登录或者注册", Toast.LENGTH_SHORT).show();
            selectFragment = new SelectFragment();
            getFragmentManager().beginTransaction().replace(R.id.activity_auth_container,selectFragment).commit();
        }
    }
}
