package diplom.oleg.client.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import diplom.oleg.client.android.FirebaseService;
import diplom.oleg.client.android.R;
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
        restClient = new RestClient(getSharedPreferences(getPackageName(), MODE_PRIVATE).getString("serverIP", "192.168.1.101"));
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .imageDownloader(firebaseService)
                .threadPoolSize(1)
                .defaultDisplayImageOptions(options)
                .diskCache(new UnlimitedDiskCache(getExternalCacheDir()))
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
