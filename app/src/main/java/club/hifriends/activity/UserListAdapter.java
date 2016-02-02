package club.hifriends.activity;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import club.hifriends.R;

/**
 * Created by heyon on 2016/2/2.
 */
public class UserListAdapter extends ArrayAdapter<Partner>{
    private int resourceID;
    public UserListAdapter(Context context, int resource, List<Partner> objects) {
        super(context, resource, objects);
        this.resourceID = resource;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        Partner partner = getItem(position);
        final View view = LayoutInflater.from(getContext()).inflate(resourceID, null);//为子项加载布局
        TextView tv_phone = (TextView)view.findViewById(R.id.tv_phone);
        TextView tv_name = (TextView)view.findViewById(R.id.tv_name);
        tv_name.setText(partner.getName());
        tv_phone.setText(partner.getPhone());
        return  view;
    }
}
