package diplom.oleg.client.android;

import java.sql.Statement;

import diplom.oleg.client.android.model.Accept;
import diplom.oleg.client.android.model.AcceptsResponse;
import diplom.oleg.client.android.model.Task;
import diplom.oleg.client.android.model.TasksResponse;
import diplom.oleg.client.android.model.User;
import diplom.oleg.client.android.model.UsersResponse;
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
    String BASIC_AUTH_HEADER = "Authorization";


    @GET("/me")
    Call<User> loadUserInfo(@Header(BASIC_AUTH_HEADER) String basicAuthString);

    @GET("/signin")
    Call<User> signin(@Query("email") String email, @Query("password") String password);

    @PATCH("/users/{userId}")
    Call<User> updateUser(@Header(BASIC_AUTH_HEADER) String basicAuth, @Path("userId") String id, @Body User user);

    @GET("/users/{userId}")
    Call<User> getUser(@Header(BASIC_AUTH_HEADER) String basicAuth, @Path("userId") String id);

    @POST("/tasks/")
    Call<Task> createTask(@Header("Content-Type") String content_type, @Header(BASIC_AUTH_HEADER) String basicAuth, @Body Task task);

    @PATCH("/tasks/{taskId}")
    Call<Task> updateTask(@Header("Content-Type") String content_type, @Header(BASIC_AUTH_HEADER) String basicAuth, @Body Task task, @Path("taskId") String taskId);

    @POST("/accepts/")
    Call<Accept> createAccept(@Header("Content-Type") String content_type, @Header(BASIC_AUTH_HEADER) String basicAuth, @Body Accept accept);

    @GET("/tasks/")
    Call<TasksResponse> getTasks(@Header(BASIC_AUTH_HEADER) String basicAuth);

    @GET("/tasks/{taskId}")
    Call<Task> getTask(@Header(BASIC_AUTH_HEADER) String basicAuth, @Path("taskId") String taskId);

    @GET("/accepts/search/findByTaskId")
    Call<AcceptsResponse> findAcceptsByTask(@Header(BASIC_AUTH_HEADER) String basicAuth, @Query("taskId") String taskId);

    @GET("/users/")
    Call<UsersResponse> getUsers(@Header(BASIC_AUTH_HEADER) String basicAuth);
}
