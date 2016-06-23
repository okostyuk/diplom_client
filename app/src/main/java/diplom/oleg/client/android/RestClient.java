package diplom.oleg.client.android;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import diplom.oleg.client.android.model.Accept;
import diplom.oleg.client.android.model.AcceptsResponse;
import diplom.oleg.client.android.model.Task;
import diplom.oleg.client.android.model.TasksResponse;
import diplom.oleg.client.android.model.User;
import diplom.oleg.client.android.model.UsersResponse;
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

    public User updateUser(String userId, User user) throws IOException {
        Response<User> res = restService.updateUser(basicAuth, userId, user).execute();
        if (res.isSuccessful())
            return  res.body();
        throw new IOException(res.errorBody().string());
    }

    public User getUser(String userId) throws IOException {
        Response<User> response = restService.getUser(basicAuth, userId).execute();
        if (response.isSuccessful())
            return response.body();
        throw new IOException(response.errorBody().string());
    }

    public Task createTask(Task task) throws IOException {
        Response<Task> response = restService.createTask(JSON_CONTENT_TYPE, basicAuth, task).execute();
        if (response.isSuccessful())
            return response.body();
        throw new IOException(response.errorBody().string());
    }

    public List<Task> getAllTasks() throws IOException {
        Response<TasksResponse> response = restService.getTasks(basicAuth).execute();
        if (response.isSuccessful())
            return response.body().getData().getTasks();
        throw new IOException(response.errorBody().string());
    }

    public void logout() {
        basicAuth = null;
    }

    public Task getTask(String taskId) throws IOException {
        Response<Task> response = restService.getTask(basicAuth, taskId).execute();
        if (response.isSuccessful())
            return response.body();
        throw new IOException(response.errorBody().string());
    }

    public Accept createAccept(Accept accept) throws IOException {
        Response<Accept> response = restService.createAccept(JSON_CONTENT_TYPE, basicAuth, accept).execute();
        if (response.isSuccessful())
            return response.body();
        throw new IOException(response.errorBody().string());
    }

    public List<Accept> getAccepts(String taskId) throws IOException {
        Response<AcceptsResponse> response = restService.findAcceptsByTask(basicAuth, taskId).execute();
        if (response.isSuccessful())
            return response.body().getData().getAccepts();
        throw new IOException(response.errorBody().string());
    }

    public List<User> getUsers() throws IOException {
        Response<UsersResponse> response = restService.getUsers(basicAuth).execute();
        if (response.isSuccessful())
            return response.body().getData().getUsers();
        throw new IOException(response.errorBody().string());
    }
}
