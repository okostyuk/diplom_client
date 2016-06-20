package diplom.oleg.client.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import diplom.oleg.client.android.R;
import diplom.oleg.client.android.model.Task;

public class FirstScreenActivity extends DrawerActivity {


    private static final String TAG = "FirstScreenActivity";
    ImageView avatar;



    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);


        findViewById(R.id.editProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstScreenActivity.this, ProfileActivity.class));
            }
        });

        findViewById(R.id.projects).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstScreenActivity.this, TaskListActivity.class));
            }
        });

        ((TextView)findViewById(R.id.fName)).setText(user.getFirstName());
        ((TextView)findViewById(R.id.lName)).setText(user.getLastName());
        avatar = (ImageView) findViewById(R.id.avatar);

        findViewById(R.id.buttonAllTasks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstScreenActivity.this, TaskListActivity.class));
            }
        });

        updateUI();
        ImageLoader.getInstance().displayImage(user.getAvatar(), avatar);
        new LoadTasks().execute();

    }

    class LoadTasks extends AsyncTask<Void, Void, List<Task>>{
        Exception exception;

        @Override
        protected List<Task> doInBackground(Void... params) {
            try {
                List<Task> tasks = restClient.getAllTasks();
                tasks.size();
            }catch (Exception ex){
                exception = ex;
            }
            return null;
        }


    }

}
