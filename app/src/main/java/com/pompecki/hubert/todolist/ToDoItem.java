package com.pompecki.hubert.todolist;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Hubert on 09/06/2015.
 */
public class ToDoItem implements Serializable {

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    private String title;
    private String description;
    // private Date deadline;
    private Priority priority = Priority.MEDIUM;
    private boolean done = false;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean isDone() {
        return done;
    }
}
