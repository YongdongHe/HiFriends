package club.hifriends.activity.adapter;

import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import club.hifriends.R;
import club.hifriends.activity.beans.Partner;

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
    public View getView(int position, final View convertView, final ViewGroup parent) {
        final Partner partner = getItem(position);
        final View view = LayoutInflater.from(getContext()).inflate(resourceID, null);//为子项加载布局
        TextView tv_phone = (TextView)view.findViewById(R.id.tv_phone);
        TextView tv_name = (TextView)view.findViewById(R.id.tv_name);
        tv_name.setText(partner.getName());
        tv_phone.setText(partner.getPhone());
        ImageView calltbn = (ImageView)view.findViewById(R.id.btn_callpartner);
        calltbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + partner.getPhone()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }catch (SecurityException e){
                    Toast.makeText(getContext(),"HiFriends尚未获得拨打电话的权限，请开启后再试。",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return  view;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        //根据listview的item数目设置宽度
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}
