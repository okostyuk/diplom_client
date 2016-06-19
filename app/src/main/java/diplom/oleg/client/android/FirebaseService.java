package diplom.oleg.client.android;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * Created by user1 on 18.06.2016.
 */

public class FirebaseService extends BaseImageDownloader implements FirebaseAuth.AuthStateListener, OnCompleteListener<AuthResult> {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String FIREBASE_GS_BASEURL = "gs://dazzling-torch-5301.appspot.com/";
    String FIREBASE_HTTP_BASEURL = "https://firebasestorage.googleapis.com/v0/b/dazzling-torch-5301.appspot.com/o/";
    //?alt=media&token=73e82fad-9fc1-404c-9678-a7ce766084a8"
    private final String TAG = "FirebaseService";
    Activity activity;

    private FirebaseUser user = null;

    public FirebaseService(Activity context) {
        super(context);
        activity = context;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Log.d(TAG, "onAuthStateChanged: " + (user != null));
    }


    @Override
    protected InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {
        try {
            if (user == null){
                authenticate();
            }
            StorageReference storageRef = storage.getReference().child(imageUri);
            Task<Uri> task = storageRef.getDownloadUrl();
            int attempts = 10;
            while(attempts > 0){
                attempts--;
                if (task.isComplete())
                    break;
                Thread.sleep(500);
            }
            return getStreamFromNetwork(task.getResult().toString(), extra);
        }catch (Exception ex){
            Log.e(TAG, "getStream: ", ex);
        }
        return null;
    }

    private void authenticate() {
        user = mAuth.signInAnonymously().getResult().getUser();

    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        FirebaseUser user = task.getResult().getUser();
        Log.d(TAG, "onComplete<AuthResult>: " + (user != null));
    }

    public void uploadFile(String pathToFile, String name){

    }
}