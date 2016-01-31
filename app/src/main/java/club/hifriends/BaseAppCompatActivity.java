package club.hifriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import club.hifriends.auth.AuthHelper;

/**
 * Created by heyon on 2016/1/29.
 */
public class BaseAppCompatActivity extends AppCompatActivity{
    AuthHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.authHelper = new AuthHelper(this);
    }

    public void showMsg(String msg){
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }
    public AuthHelper getAuthHepler(){
        return authHelper;
    }

    public void startActivityAndFinish(Intent intent){
        startActivity(intent);
        finish();
    }
}
