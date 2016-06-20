package diplom.oleg.client.android;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;

import diplom.oleg.client.android.model.Task;
import diplom.oleg.client.android.model.User;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user1 on 15.06.2016.
 */

public class RestClient {
    private static final String TAG = "RestClient";
    private static final String JSON_CONTENT_TYPE = "application/json";
    static String basicAuth;
    private final RestService restService;

    public RestClient(String ip_or_hostname) {
        restService = new Retrofit.Builder()
                .baseUrl("http://" + ip_or_hostname + ":8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RestService.class);
    }


    public User login(String email, String password) throws IOException {
        Response<User> response = restService.signin(email, password).execute();
        if (!response.isSuccessful()){
            throw new IOException(response.errorBody().string());
        }

        String credentials = email + ":" + password;
        basicAuth ="Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return response.body();
    }

    public void updateUser(String userId, User user) throws IOException {
        Response<Void> res = restService.updateUser(basicAuth, userId, user).execute();
        if (!res.isSuccessful())
            throw new IOException(res.errorBody().string());
    }

    public User getUser(String userId) throws IOException {
        Response<User> response = restService.getUser(basicAuth, userId).execute();
        if (response.isSuccessful())
            return response.body();
        throw new IOException(response.errorBody().string());
    }

    public void createTask(Task task) throws IOException {
        restService.createTask(JSON_CONTENT_TYPE, basicAuth, task).execute();
    }
}
