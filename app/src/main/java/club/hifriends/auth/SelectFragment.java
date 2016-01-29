package club.hifriends.auth;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import club.hifriends.R;

/**
 * Created by heyon on 2016/1/28.
 */
public class SelectFragment extends Fragment {
    View fragmentContainer;
    public SelectFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获得fragment的布局
        fragmentContainer = inflater.inflate(R.layout.fragment_auth_select,container,false);
        //为登录和注册按钮绑定跳转
        Button btn_login = (Button)fragmentContainer.findViewById(R.id.auth_btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity().getBaseContext(),LoginActivity.class));
            }
        });
        Button btn_register = (Button)fragmentContainer.findViewById(R.id.auth_btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity().getBaseContext(),RegisterActivity.class));
            }
        });
        return fragmentContainer;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
