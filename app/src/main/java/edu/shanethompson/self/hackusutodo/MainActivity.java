package edu.shanethompson.self.hackusutodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TodoManager.TodoManagerInterface {

    private TodoManager mManager = new TodoManager(this);
    private List<Todo> mTodoList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private EditText mTodoInput;
    TodoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpFilterButtons();

        setUpTodoInput();

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new TodoAdapter(mTodoList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mAdapter);
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
                    }
                    return false;
                }
            });
        }
    }

    private void addTodoItem(String task) {
        mManager.addTodo(new Todo(task, false));
        mAdapter.notifyDataSetChanged();
        mTodoInput.setText("");
    }

    @Override
    public void onListRefresh(List<Todo> currentList) {
        mTodoList = currentList;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if(mTodoInput != null && mTodoInput.hasFocus() && mTodoInput.getText().toString().length() > 0) {
            mManager.addTodo(new Todo(mTodoInput.getText().toString(), false));
        }
        super.onBackPressed();
    }
}
