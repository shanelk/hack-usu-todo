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
    private TodoFilter filter = TodoFilter.ALL;

    public TodoManager(TodoManagerInterface listener) {
        this.listener = listener;
        fullList = new ArrayList<>();
        sendListToListener();
    }

    public void setFilter(TodoFilter filter) {
        this.filter = filter;
        sendListToListener();
    }

    public void addTodo(Todo todo) {
        fullList.add(todo);
        sendListToListener();
    }

    public void removeTodo(Todo todo) {
        fullList.remove(todo);
        sendListToListener();
    }

    private void sendListToListener() {
        Collections.sort(fullList);
        switch (filter) {
            case ALL:
                listener.onListRefresh(fullList);
                break;
            case ACTIVE:
                listener.onListRefresh(activeList());
                break;
            case COMPLETE:
                listener.onListRefresh(completeList());
                break;
        }
    }

    private List<Todo> completeList() {
        List<Todo> filteredList = new ArrayList<>();
        for(Todo item : fullList) {
            if(item.isComplete()) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    private List<Todo> activeList() {
        List<Todo> filteredList = new ArrayList<>();
        for(Todo item : fullList) {
            if(!item.isComplete()) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    public void clearComplete() {
        for(int i = fullList.size() - 1; i >= 0; i--) {
            Todo todo = fullList.get(i);
            if(todo.isComplete()) {
                fullList.remove(todo);
            }
        }
        sendListToListener();
    }

    public void toggleComplete(int i) {
        fullList.get(i).setComplete(!fullList.get(i).isComplete());
        sendListToListener();
    }

    interface TodoManagerInterface {
        public void onListRefresh(List<Todo> currentList);
    }

    enum TodoFilter {
        ALL,
        ACTIVE,
        COMPLETE
    }
}
