package diplom.oleg.client.android;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by user1 on 18.06.2016.
 */

public class FirebaseImageDownloader implements ImageDownloader {

    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    public InputStream getStream(String imageUri, Object extra) throws IOException {
        StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");
        return null;
    }
}
