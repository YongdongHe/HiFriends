package club.hifriends.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;

import club.hifriends.R;

/**
 * Created by heyon on 2016/1/31.
 */
public class ActivityListAdapter extends ArrayAdapter<ActivityItem> {
    private int resourceID;
    public ActivityListAdapter(Context context, int resource, List<ActivityItem> objects) {
        super(context, resource, objects);
        this.resourceID = resource;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        ActivityItem activityItem = getItem(position);
        final View view = LayoutInflater.from(getContext()).inflate(resourceID, null);//为子项加载布局
        TextView tv_tips = (TextView)view.findViewById(R.id.tv_activity_item_tips);
        TextView tv_content = (TextView)view.findViewById(R.id.tv_activity_item_content);
        TextView tv_time = (TextView)view.findViewById(R.id.tv_activity_item_time);
        tv_tips.setText(activityItem.getTips());
        tv_content.setText(activityItem.getDescription());
        tv_time.setText(activityItem.getTime());
        return view;

    }

}
