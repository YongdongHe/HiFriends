package club.hifriends.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



/**
 * Created by heyon on 2016/1/31.
 */
public class ActivityHelper {
    public static ArrayList<ActivityItem> transfromJSONtoArrayList(JSONArray jsonArray)
            throws JSONException{
        ArrayList<ActivityItem> list = new ArrayList<>();
        //遍历json数组
        Log.d("activityHelper",jsonArray.length()+"");
        for(int i = 0;i<jsonArray.length();i++){
            JSONObject activityItem = jsonArray.optJSONObject(i);
            list.add(new ActivityItem(
                    activityItem.getString("leader"),
                    activityItem.getString("time"),
                    activityItem.getString("label"),
                    activityItem.getString("id"),
                    activityItem.getString("title"),
                    activityItem.getString("content")
            ));
        }
        return list;
    }



}
