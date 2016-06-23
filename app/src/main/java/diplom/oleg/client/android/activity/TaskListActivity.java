package diplom.oleg.client.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import diplom.oleg.client.android.R;

import diplom.oleg.client.android.model.Task;

import java.util.List;

/**
 * An activity representing a list of Tasks. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TaskDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class TaskListActivity extends DrawerActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static final int CREATE_TASK_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_task_list);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(TaskListActivity.this, CreateTaskActivity.class), CREATE_TASK_REQUEST);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.task_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        assert recyclerView != null;
        mSwipeRefreshLayout.setOnRefreshListener(this);
        new LoadTasks().execute();
    }

    @Override
    public void onRefresh() {
        new LoadTasks().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_TASK_REQUEST && resultCode == RESULT_OK){
            new LoadTasks().execute();
        }
    }

    class LoadTasks extends AsyncTask<Void, Void, List<Task>> {
        Exception exception;

        @Override
        protected List<Task> doInBackground(Void... params) {
            try {
                List<Task> tasks = restClient.getAllTasks();
                return tasks;
            }catch (Exception ex){
                exception = ex;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            mSwipeRefreshLayout.setRefreshing(false);
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(tasks));
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Task> mValues;

        public SimpleItemRecyclerViewAdapter(List<Task> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getId());
            holder.mContentView.setText(mValues.get(position).getTitle());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, TaskDetailActivity.class);
                    intent.putExtra(TaskDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Task mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
