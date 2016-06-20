package diplom.oleg.client.android;

import diplom.oleg.client.android.model.Task;
import diplom.oleg.client.android.model.User;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by user1 on 11.06.2016.
 */

public interface RestService {


    @GET("/me")
    Call<User> loadUserInfo(@Header("Authorization") String basicAuthString);

    @GET("/signin")
    Call<User> signin(@Query("email") String email, @Query("password") String password);

    @PATCH("/users/{userId}")
    Call<Void> updateUser(@Header(value = "Authentication") String basicAuth, @Path("userId") String id, @Body User user);

    @GET("/users/{userId}")
    Call<User> getUser(@Header(value = "Authentication") String basicAuth, @Path("userId") String id);

    @PUT("/tasks")
    Call<Void> createTask(@Header("Content-Type") String content_type, @Header(value = "Authentication") String basicAuth, @Body Task task);


}
