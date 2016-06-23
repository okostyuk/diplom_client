package diplom.oleg.client.android.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import diplom.oleg.client.android.R;
import diplom.oleg.client.android.model.Accept;
import diplom.oleg.client.android.model.Task;
import diplom.oleg.client.android.model.User;

/**
 * An activity representing a single Task detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link TaskListActivity}.
 */
public class TaskDetailActivity extends DrawerActivity {

    String taskId;
    RecyclerView recyclerView;
    Map<String, User> usersMap = new ConcurrentHashMap<>();
    AcceptsAdapter acceptsAdapter;

    TextView desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_task_drawer);
        taskId = getIntent().getStringExtra(TaskDetailFragment.ARG_ITEM_ID);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        desc = (TextView) findViewById(R.id.desc);
        acceptsAdapter = new AcceptsAdapter();
        recyclerView.setAdapter(acceptsAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddAcceptTask().execute();
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        new LoadUsersTask().execute();
        new LoadDataTask(taskId).execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, TaskListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class LoadDataTask extends AsyncTask<Void, Void, Void> {
        private final String taskId;
        Exception exception;

        Task task;
        List<Accept> accepts;

        public LoadDataTask(String taskId) {
            this.taskId = taskId;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                task = restClient.getTask(taskId);
                accepts = restClient.getAccepts(taskId);
                acceptsAdapter.setAccepts(accepts);
            } catch (Exception ex) {
                exception = ex;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setTitle(task.getTitle());
            desc.setText(task.getDesc());
        }
    }

    private class AddAcceptTask extends AsyncTask<Void, Void, Accept> {
        Exception exception;

        @Override
        protected Accept doInBackground(Void... params) {
            try {
                Accept accept = new Accept();
                accept.setPerformerId(user.getId());
                accept.setTaskId(taskId);
                accept = restClient.createAccept(accept);
                return accept;
            } catch (IOException e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Accept accept) {
            if (exception != null)
                return;//TODO
            acceptsAdapter.add(accept);
            acceptsAdapter.notifyItemInserted(acceptsAdapter.getItemCount()-1);
        }
    }

    private class LoadUsersTask extends AsyncTask<Void, Void, List<User>> {
        Exception exception;
        @Override
        protected List<User> doInBackground(Void... params) {
            try{
                List<User> users = restClient.getUsers();
                usersMap.clear();
                for (User user : users){
                    usersMap.put(user.getId(), user);
                }
            }catch (Exception ex){
                exception = ex;
            }
            return null;
        }
    }

    private class AcceptsAdapter extends RecyclerView.Adapter<AcceptsAdapter.ViewHolder>{
        List<Accept> accepts = new ArrayList<>();

        void add(Accept accept){
            accepts.add(accept);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.accept_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Accept accept = accepts.get(position);
            User user = usersMap.get(accept.getPerformerId());
            holder.name.setText(user.getFirstName() + " " + user.getLastName());
            holder.text.setText(accept.getText());
            ImageLoader.getInstance().displayImage(user.getAvatar(), holder.avatar);
        }

        @Override
        public int getItemCount() {
            return accepts.size();
        }

        public void setAccepts(List<Accept> accepts) {
            this.accepts = accepts;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            final ImageView avatar;
            final TextView name;
            final TextView text;
            final Button chooseButton;
            public ViewHolder(View itemView) {
                super(itemView);
                avatar = (ImageView) itemView.findViewById(R.id.avatar);
                name = (TextView) itemView.findViewById(R.id.name);
                text = (TextView) itemView.findViewById(R.id.text);
                chooseButton = (Button) itemView.findViewById(R.id.chooseButton);
                chooseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long itemId = getItemId();
                        Accept accept = accepts.get((int) itemId);
                    }
                });
            }
        }
    }
}
