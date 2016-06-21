package diplom.oleg.client.android.model;


import com.google.gson.annotations.SerializedName;

/**
 * Created by alena on 20.06.16.
 */
public class AcceptsResponse {

@SerializedName("_embedded")
    AcceptsEmbedded data;
    Page page;

    public AcceptsResponse() {
    }

    public AcceptsEmbedded getData() {
        return data;
    }

    public void setData(AcceptsEmbedded data) {
        this.data = data;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
