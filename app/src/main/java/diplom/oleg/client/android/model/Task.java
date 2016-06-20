package diplom.oleg.client.android.model;

/**
 * Created by user1 on 11.06.2016.
 */

public class Task {
    String id;
    String title;
    String desc;
    String ownerId;
    String performerId;

    public Task() {
    }

    public Task(String title, String desc, String ownerId) {
        this.title = title;
        this.desc = desc;
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPerformerId() {
        return performerId;
    }

    public void setPerformerId(String performerId) {
        this.performerId = performerId;
    }
}
