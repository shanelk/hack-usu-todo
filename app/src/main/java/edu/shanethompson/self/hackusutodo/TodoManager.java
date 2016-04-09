package edu.shanethompson.self.hackusutodo;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
    private SharedPreferences prefs;
    private static final String PREFS_KEY = "todo_json_key";

    public TodoManager(TodoManagerInterface listener, SharedPreferences prefs) {
        this.listener = listener;
        this.prefs = prefs;
        fullList = loadFromPrefs();
        sendListToListener();
    }

    private List<Todo> loadFromPrefs() {
        String jsonString = prefs.getString(PREFS_KEY, "");
        if(jsonString.isEmpty()) {
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Todo>>(){}.getType();
        return gson.fromJson(jsonString, type);
    }

    private void saveToPrefs() {
        String jsonString;
        Gson gson = new Gson();
        jsonString = gson.toJson(fullList);
        prefs.edit().putString(PREFS_KEY, jsonString).apply();
    }

    public void save() {
        saveToPrefs();
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

    public void clearAll() {
        prefs.edit().remove(PREFS_KEY).apply();
        fullList.clear();
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
