package diplom.oleg.client.android.model;

import java.util.List;

/**
 * Created by alena on 20.06.16.
 */
public class Embedded {
    List<Task> tasks;

    public Embedded() {
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
