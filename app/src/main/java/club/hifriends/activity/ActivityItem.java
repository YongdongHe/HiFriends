package club.hifriends.activity;

/**
 * Created by heyon on 2016/1/31.
 */
public class ActivityItem {
    private String tips;//顶端提示
    private String leader;
    private String time;
    private String label;
    private String activity_id;
    private String description;//活动描述
    public ActivityItem(String leader, String time, String label, String activity_id, String description) {
        this.leader = leader;
        this.time = time;
        this.label = label;
        this.activity_id = activity_id;
        this.description = description;
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

    public String getDescription() {
        return description;
    }
}
