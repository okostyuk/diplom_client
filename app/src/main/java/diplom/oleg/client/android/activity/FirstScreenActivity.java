package diplom.oleg.client.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
    TextView firstName;
    TextView lastName;


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main_drawer);
        avatar = (ImageView) findViewById(R.id.avatar);
        firstName  = ((TextView)findViewById(R.id.fName));
        lastName = ((TextView)findViewById(R.id.lName));

        if (user.getFirstName() == null || user.getFirstName().isEmpty())
            startActivity(new Intent(this, ProfileActivity.class));


        findViewById(R.id.editProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstScreenActivity.this, ProfileActivity.class));
            }
        });


        findViewById(R.id.buttonAllTasks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstScreenActivity.this, TaskListActivity.class));
            }
        });

        //new LoadTasks().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    protected void updateUI(){
        super.updateUI();
        firstName.setText((user.getFirstName()));
        lastName.setText(user.getLastName());

        if (user.getAvatar() == null){
            avatar.setImageResource(R.drawable.avatar_empty);
        }else{
            ImageLoader.getInstance().displayImage(user.getAvatar(), avatar);
        }
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
