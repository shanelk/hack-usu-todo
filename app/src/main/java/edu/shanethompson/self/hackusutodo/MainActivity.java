package edu.shanethompson.self.hackusutodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TodoManager.TodoManagerInterface, TodoAdapter.TodoClickedListener {

    private List<Todo> mTodoList = new ArrayList<>();
    private TodoManager mManager = new TodoManager(this);
    private EditText mTodoInput;
    TodoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManager.addTodo(new Todo("Hi", false));
        setContentView(R.layout.activity_main);

        setUpFilterButtons();

        setUpTodoInput();

        setUpListView();

    }

    private void setUpListView() {
        ListView mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new TodoAdapter(this, R.layout.list_todo_item, mTodoList, this);
        if(mListView != null) {
            mListView.setAdapter(mAdapter);
        }
    }

    private void setUpFilterButtons() {
        Button allButton = (Button) findViewById(R.id.all_button);
        Button activeButton = (Button) findViewById(R.id.active_button);
        Button completeButton = (Button) findViewById(R.id.complete_button);

        if(allButton != null) allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allButtonClicked();
            }
        });

        if(activeButton != null) activeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeButtonClicked();
            }
        });

        if(completeButton != null) completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeButtonClicked();
            }
        });
    }

    private void activeButtonClicked() {
        mManager.setFilter(TodoManager.TodoFilter.ACTIVE);
    }

    private void completeButtonClicked() {
        mManager.setFilter(TodoManager.TodoFilter.COMPLETE);
    }

    private void allButtonClicked() {
        mManager.setFilter(TodoManager.TodoFilter.ALL);
    }

    private void setUpTodoInput() {
        mTodoInput = (EditText) findViewById(R.id.todo_input);
        if(mTodoInput != null) {
            mTodoInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        addTodoItem(v.getText().toString());
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void todoClicked(int position) {
        mManager.toggleComplete(position);
    }

    private void addTodoItem(String task) {
        mManager.addTodo(new Todo(task, false));
        mAdapter.notifyDataSetChanged();
        mTodoInput.setText("");
    }

    @Override
    public void onListRefresh(List<Todo> currentList) {
        mTodoList.clear();
        mTodoList.addAll(currentList);
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        if(mTodoInput != null && mTodoInput.hasFocus() && mTodoInput.getText().toString().length() > 0) {
            addTodoItem(mTodoInput.getText().toString());
        }
        super.onBackPressed();
    }
}
