package club.hifriends.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import club.hifriends.R;
import club.hifriends.activity.LookInfoActivity;
import club.hifriends.activity.beans.ActivityItem;


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
        final ActivityItem activityItem = getItem(position);
        final View view = LayoutInflater.from(getContext()).inflate(resourceID, null);//为子项加载布局
        TextView tv_tips = (TextView)view.findViewById(R.id.tv_activity_item_tips);
        TextView tv_content = (TextView)view.findViewById(R.id.tv_activity_item_content);
        TextView tv_time = (TextView)view.findViewById(R.id.tv_activity_item_time);
        TextView tv_title = (TextView)view.findViewById(R.id.tv_activity_item_title);
        tv_tips.setText(activityItem.getTips());
        tv_content.setText(activityItem.getContent());
        tv_time.setText(activityItem.getTime());
        tv_title.setText(activityItem.getTitle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("activity_id", activityItem.getActivity_id());
                Intent intent = new Intent(getContext(),LookInfoActivity.class);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });
        return view;

    }

}
