package edu.shanethompson.self.hackusutodo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * Created by shanethompson on 4/8/16.
 *
 */

public class TodoManager {
    private TodoManagerInterface listener;
    private List<Todo> fullList;

    public TodoManager(TodoManagerInterface listener) {
        this.listener = listener;
        fullList = new ArrayList<>();
        listener.onListRefresh(fullList);
    }

    public void addTodo(Todo todo) {
        fullList.add(todo);
        Collections.sort(fullList);
        listener.onListRefresh(fullList);
    }

    public void removeTodo(Todo todo) {
        fullList.remove(todo);
    }

    public void showComplete() {
        List<Todo> filteredList = new ArrayList<>();
        for(Todo item : fullList) {
            if(item.isComplete()) {
                filteredList.add(item);
            }
        }
        listener.onListRefresh(filteredList);
    }

    interface TodoManagerInterface {
        public void onListRefresh(List<Todo> currentList);
    }
}
