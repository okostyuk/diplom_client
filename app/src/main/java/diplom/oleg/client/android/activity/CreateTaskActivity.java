package diplom.oleg.client.android.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import diplom.oleg.client.android.R;
import diplom.oleg.client.android.RestClient;
import diplom.oleg.client.android.model.Task;


public class CreateTaskActivity extends Activity {

    Toolbar toolbar;
    RestClient restClient;
    EditText title;
    EditText description;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.create_task);
        userId = getSharedPreferences(getPackageName(), MODE_PRIVATE).getString("userId", "");
        restClient = new RestClient(getSharedPreferences(getPackageName(), MODE_PRIVATE).getString("serverIP", ""));

        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateTask(
                        title.getText().toString(),
                        description.getText().toString(),
                        userId
                ).execute();
            }
        });
    }

    private class CreateTask extends AsyncTask<Void, Void, Void> {

        private final String title;
        private final String desc;
        private final String userId;

        public CreateTask(String title, String desc, String userId) {
            this.title = title;
            this.desc = desc;
            this.userId = userId;
        }

        @Override
        protected Void doInBackground(Void... params) {

            restClient.createTask(new Task(title, desc, userId));
            return null;
        }
    }
}
