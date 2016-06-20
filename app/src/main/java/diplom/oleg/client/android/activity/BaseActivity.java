package diplom.oleg.client.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import diplom.oleg.client.android.FirebaseService;
import diplom.oleg.client.android.RestClient;
import diplom.oleg.client.android.model.User;

/**
 * Created by alena on 20.06.16.
 */
public class BaseActivity extends AppCompatActivity {

    FirebaseService firebaseService;
    RestClient restClient;
    protected static User user;


    protected void onCreate(@Nullable Bundle savedInstanceState, int layoutRes) {
        super.onCreate(savedInstanceState);
        setContentView(layoutRes);

        firebaseService = new FirebaseService(this);
        restClient = new RestClient(getSharedPreferences(getPackageName(), MODE_PRIVATE).getString("serverIP", ""));
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .imageDownloader(firebaseService)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
