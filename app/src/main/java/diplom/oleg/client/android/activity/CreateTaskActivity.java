package diplom.oleg.client.android.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import diplom.oleg.client.android.R;
import diplom.oleg.client.android.model.Task;


public class CreateTaskActivity extends DrawerActivity {

    EditText title;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_create_task_drawer);
        toolbar.setTitle(R.string.create_task);
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);

        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateTask(
                        title.getText().toString(),
                        description.getText().toString(),
                        user.getId()
                ).execute();
            }
        });
    }

    private class CreateTask extends AsyncTask<Void, Void, Void> {
        Exception exception;

        private final String title;
        private final String desc;
        private final String userId;

        public CreateTask(String title, String desc, String userId) {
            this.title = title;
            this.desc = desc;
            this.userId = userId;
        }

        @Override
        protected void onPreExecute() {
            toolbar.setTitle("Creating");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                restClient.createTask(new Task(title, desc, userId));
            }catch (Exception ex){
                exception = ex;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (exception == null) {
                toolbar.setTitle("Created");
                setResult(RESULT_OK, null);
                finish();
            }else
                toolbar.setTitle("Error");
        }
    }
}
