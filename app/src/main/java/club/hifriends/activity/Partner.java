package club.hifriends.activity;

/**
 * Created by heyon on 2016/2/2.
 */
public class Partner {
    private String name;
    private String id;
    private String phone;

    public Partner(String name, String id, String phone) {
        this.name = name;
        this.id = id;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }
}
