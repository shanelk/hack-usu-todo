package edu.shanethompson.self.hackusutodo;

/**
 *
 * Created by shanethompson on 4/8/16.
 *
 */

public class Todo implements Comparable<Todo> {
    private String task;
    private boolean complete;

    @Override
    public int compareTo(Todo another) {
        if(this.complete && another.complete || !this.complete && !another.complete) return 0;

        if(!this.complete) {
            return -1;
        } else {
            return 1;
        }
    }

    public Todo(String task, boolean complete) {
        this.task = task; this.complete = complete;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
