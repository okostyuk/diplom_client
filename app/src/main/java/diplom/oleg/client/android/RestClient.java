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
    static RestService restService = new Retrofit.Builder()
            .baseUrl("http://192.168.1.101:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RestService.class);



    public static void init(String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.101:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restService = retrofit.create(RestService.class);
    }

    public static User login(String email, String password) throws IOException {
        Response<User> response = restService.signin(email, password).execute();
        if (!response.isSuccessful()){
            throw new IOException(response.errorBody().string());
        }

        String credentials = email + ":" + password;
        basicAuth ="Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return response.body();
    }
}
