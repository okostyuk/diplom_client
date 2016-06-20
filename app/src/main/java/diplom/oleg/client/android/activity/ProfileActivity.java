package diplom.oleg.client.android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import diplom.oleg.client.android.FirebaseService;
import diplom.oleg.client.android.R;
import diplom.oleg.client.android.RestClient;
import diplom.oleg.client.android.model.User;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_PROFILE_AVATAR_GALLERY = 1;
    private static final String TAG = "ProfileActivity";
    TextView fname;
    TextView lname;
    TextView email;
    TextView desc;
    ImageView avatar;
    User user;
    Uri avatarUri = null;
    Toolbar toolbar;
    String userId = null;
    FirebaseService firebaseService;
    RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseService = new FirebaseService(this);
        restClient = new RestClient(getSharedPreferences(getPackageName(), MODE_PRIVATE).getString("serverIP", ""));
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .imageDownloader(firebaseService)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fname = (TextView) findViewById(R.id.fName);
        lname = (TextView) findViewById(R.id.lName);
        email = (TextView) findViewById(R.id.email);
        desc = (TextView) findViewById(R.id.desc);
        avatar = (ImageView) findViewById(R.id.avatar);
        fname = (TextView) findViewById(R.id.fName);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, REQUEST_PROFILE_AVATAR_GALLERY);

            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveTask().execute();
            }
        });


        toolbar.setTitle("LOading");
        new AsyncTask<Void,Void,User>(){
            Exception ex;
            @Override
            protected User doInBackground(Void... params) {
                SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                userId = prefs.getString("userId", "");
                try {
                    return restClient.getUser(userId);
                } catch (IOException e) {
                    Log.e(TAG, "getUser doInBackground: ", e);
                    ex = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(User u) {
                Log.d(TAG, "onPostExecute: User is " + u);
                if (ex != null)
                    Toast.makeText(ProfileActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                toolbar.setTitle("Profile");
                if (u != null){
                    user = u;
                    updateUI(u);
                }else{
                }
            }
        }.execute();
    }

    private void updateUI(User user) {
        fname.setText(user.getFirstName());
        lname.setText(user.getLastName());
        desc.setText(user.getDescription());
        email.setText(user.getEmail());
        if (user.getAvatar() != null){
            ImageLoader.getInstance().displayImage(user.getAvatar(), avatar);
        }else{
            avatar.setImageResource(R.drawable.ic_perm_identity_black_24dp);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ///getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, FirstScreenActivity.class));
        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_tasks) {
            startActivity(new Intent(this, TaskListActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == REQUEST_PROFILE_AVATAR_GALLERY){
            avatarUri = data.getData();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageLoader.getInstance().displayImage(avatarUri.toString(), avatar);
                }
            });
        }
    }

    private class SaveTask extends AsyncTask<Void,Void,Void>{
        Exception ex;
        @Override
        protected void onPreExecute() {
            toolbar.setTitle("Saving");
            user.setFirstName(fname.getText().toString());
            user.setLastName(lname.getText().toString());
            user.setDescription(desc.getText().toString());
            user.setEmail(email.getText().toString());
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (avatarUri != null){
                    String avatarImage = saveImageToFirebase();
                    user.setAvatar(avatarImage);
                }
                restClient.updateUser(userId, user);
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: ", e);
                ex = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (ex != null){
                toolbar.setTitle("Error");
            }else{
                toolbar.setTitle("Saved");
            }
        }
    }

    private String saveImageToFirebase() {
        try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), avatarUri);
            float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
            int width = 120;
            int height = Math.round(width / aspectRatio);

            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            String filename = "avatar_"+userId+".png";
            firebaseService.uploadFile(data, filename);
            avatarUri = null;
            return filename;
        }catch (Exception ex){
            return user.getAvatar();
        }

    }
}
