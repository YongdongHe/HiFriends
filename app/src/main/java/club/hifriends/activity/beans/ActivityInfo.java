package club.hifriends.activity.beans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by heyon on 2016/2/2.
 */
public class ActivityInfo {
    private String leader;//活动发起者
    private String time;
    private String label;//活动标签
    private String activity_id;//活动id
    private String title;
    private String content;//活动内容
    private ArrayList<Partner> partnerArrayList;
    public ActivityInfo (JSONObject response)throws JSONException{
        this.leader = response.getString("leader");
        this.time = response.getString("time");
        this.label = response.getString("label");
        this.activity_id = response.getString("activity_id");
        this.title = response.getString("title");
        this.content = response.getString("content");
        partnerArrayList = new ArrayList<>();
        JSONArray partners = response.getJSONArray("partners");
        for(int i = 0;i<partners.length();i++){
            JSONObject partner = partners.optJSONObject(i);
            partnerArrayList.add(new Partner(
                            partner.getString("name"),
                            partner.getString("id"),
                            partner.getString("phone")
            ));
        }
    }


    public String getLeader() {
        return leader;
    }

    public String getTime() {
        return time;
    }

    public String getLabel() {
        return label;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<Partner> getPartnerArrayList() {
        return partnerArrayList;
    }
}
