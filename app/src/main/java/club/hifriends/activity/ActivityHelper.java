package club.hifriends.activity;

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
        for(int i = 1;i<=jsonArray.length();i++){
            JSONObject activityItem = jsonArray.optJSONObject(i);
            list.add(new ActivityItem(
                    activityItem.getString("leader"),
                    activityItem.getString("time"),
                    activityItem.getString("des"),
                    activityItem.getString("id"),
                    activityItem.getString("activity")
            ));
        }
        return list;
    }
}
