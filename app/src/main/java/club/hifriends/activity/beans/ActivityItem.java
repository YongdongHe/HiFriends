package club.hifriends.activity.beans;

/**
 * Created by heyon on 2016/1/31.
 */
public class ActivityItem {
    private String tips;//顶端提示
    private String leader;//活动发起者
    private String time;
    private String label;//活动标签
    private String activity_id;//活动id
    private String title;
    private String content;//活动内容
    public ActivityItem(String leader, String time, String label, String activity_id, String title,String content) {
        this.leader = leader;
        this.time = time;
        this.label = label;
        this.activity_id = activity_id;
        this.title = title;
        this.content = content;
    }

    public String getTips() {
        return leader+" 发起了活动";
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

    public String getTitle(){
        return title;
    }

    public String getContent() {
        return content;
    }
}
