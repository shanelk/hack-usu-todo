package edu.shanethompson.self.hackusutodo;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TodoManager.TodoManagerInterface, TodoAdapter.TodoClickedListener {

    private List<Todo> mTodoList = new ArrayList<>();
    private TodoManager mManager;
    private RelativeLayout mMainContentView;
    private EditText mTodoInput;
    TodoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mManager = new TodoManager(this, PreferenceManager.getDefaultSharedPreferences(this));
        mMainContentView = (RelativeLayout) findViewById(R.id.content_view);

        setUpToolbar();

        setUpFilterButtons();

        setUpTodoInput();

        setUpListView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mManager.save();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.todo_toolbar);
        setSupportActionBar(toolbar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_clear_complete:
                mManager.clearComplete();
                return true;
            case R.id.action_settings:
                settingsClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void settingsClicked() {
        Snackbar.make(mMainContentView, R.string.settings_clicked, Snackbar.LENGTH_LONG).show();
    }
}
