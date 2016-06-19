package diplom.oleg.client.android;

import diplom.oleg.client.android.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by user1 on 11.06.2016.
 */

public interface RestService {


    @GET("/me")
    Call<User> loadUserInfo(@Header("Authorization") String basicAuthString);

    @GET("/signin")
    Call<User> signin(@Query("email") String email, @Query("password") String password);
}
