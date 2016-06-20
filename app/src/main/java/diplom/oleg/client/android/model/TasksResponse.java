package diplom.oleg.client.android.model;


import com.google.gson.annotations.SerializedName;

/**
 * Created by alena on 20.06.16.
 */
public class TasksResponse {

@SerializedName("_embedded")
    Embedded data;
    Page page;

    public TasksResponse() {
    }

    public Embedded getData() {
        return data;
    }

    public void setData(Embedded data) {
        this.data = data;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
