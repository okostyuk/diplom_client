package diplom.oleg.client.android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import diplom.oleg.client.android.R;
import diplom.oleg.client.android.model.User;

public class ProfileActivity extends DrawerActivity
        /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    private static final int REQUEST_PROFILE_AVATAR_GALLERY = 1;
    private static final String TAG = "ProfileActivity";
    TextView fname;
    TextView lname;
    TextView email;
    TextView desc;
    ImageView avatar;

    Uri avatarUri = null;
    String userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_profile2);
        setContentView(R.layout.activity_profile2);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



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


        toolbar.setTitle(getResources().getString(R.string.loading));
        new LoadUserTask().execute();
    }



    protected void updateUI() {
        fname.setText(user.getFirstName());
        lname.setText(user.getLastName());
        desc.setText(user.getDescription());
        email.setText(user.getEmail());

        if (user.getAvatar() != null){
            ImageLoader.getInstance().displayImage(user.getAvatar(), avatar);
        }else{
            avatar.setImageResource(R.drawable.avatar_empty);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                updateUI();
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

    private class LoadUserTask extends AsyncTask<Void,Void,User>{
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
                updateUI();
            }else{

            }
        }
    }
}
