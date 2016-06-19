package diplom.oleg.client.android;

import android.util.Base64;

import java.io.IOException;

import diplom.oleg.client.android.model.User;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user1 on 15.06.2016.
 */

public class RestClient {
    static String basicAuth;
    static RestService restService;



    public static void init(String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.101:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        String credentials = email + ":" + password;
        basicAuth ="Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        restService = retrofit.create(RestService.class);
    }

    public static User login() throws IOException {
        Response<User> response = restService.loadUserInfo(basicAuth).execute();
        if (!response.isSuccessful()){
            throw new IOException(response.errorBody().string());
        }

        return response.body();
    }
}
