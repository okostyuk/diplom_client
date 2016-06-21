package diplom.oleg.client.android.model;


import com.google.gson.annotations.SerializedName;

/**
 * Created by alena on 20.06.16.
 */
public class UsersResponse {

@SerializedName("_embedded")
    UsersEmbedded data;
    Page page;

    public UsersResponse() {
    }

    public UsersEmbedded getData() {
        return data;
    }

    public void setData(UsersEmbedded data) {
        this.data = data;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
