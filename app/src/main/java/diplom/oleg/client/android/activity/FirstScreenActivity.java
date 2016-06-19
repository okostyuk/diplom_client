package diplom.oleg.client.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import diplom.oleg.client.android.FirebaseImageDownloader;
import diplom.oleg.client.android.FirebaseService;
import diplom.oleg.client.android.R;

public class FirstScreenActivity extends AppCompatActivity {


    private static final String TAG = "FirstScreenActivity";
    ImageView avatar;


    //private FirebaseService firebaseService;

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(firebaseService);
        //mAuth.signInAnonymously().addOnCompleteListener(firebaseService);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseService firebaseService = new FirebaseService(this);


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .imageDownloader(firebaseService)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

        findViewById(R.id.editProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstScreenActivity.this, ProfileActivity.class));
            }
        });

        findViewById(R.id.projects).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstScreenActivity.this, TaskListActivity.class));
            }
        });

        avatar = (ImageView) findViewById(R.id.avatar);
        ImageLoader.getInstance().displayImage("avatar1.png", avatar);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    }

}
