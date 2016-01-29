package club.hifriends;

import android.app.Activity;
import android.widget.Toast;

import club.hifriends.auth.AuthHelper;

/**
 * Created by heyon on 2016/1/29.
 */
public class BaseActivity extends Activity {
    AuthHelper authHelper = new AuthHelper(getBaseContext());
    public void showMsg(String msg){
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public AuthHelper getAuthHepler(){
        return authHelper;
    }
}
