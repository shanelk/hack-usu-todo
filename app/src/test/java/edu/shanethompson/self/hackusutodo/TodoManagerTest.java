package edu.shanethompson.self.hackusutodo;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 *
 * Created by shanethompson on 4/8/16.
 *
 */

//@RunWith(CustomRoboelectricRunner.class)
//@Config(constants = BuildConfig.class, sdk = 21)
public class TodoManagerTest {

    private TodoManager manager;
    private TestManagerInterface todoInterface;

    @Before
    public void setUp() throws Exception {
        todoInterface = new TestManagerInterface();
        manager = new TodoManager(todoInterface);
    }

    @Test
    public void testOnListRefresh() throws Exception {
        assertNotNull("todoList is null", todoInterface.todoList);
    }

    @Test
    public void testOnAddTodo() throws Exception {
        int startingCount = todoInterface.todoList.size();
        manager.addTodo(new Todo("Create todo app", false));
        assertEquals(startingCount + 1, todoInterface.todoList.size());
    }

    @Test
    public void testRemoveTodo() throws Exception {
        Todo firstTodo = new Todo("first todo", false);
        Todo secondTodo = new Todo("second todo", false);
        manager.addTodo(firstTodo);
        manager.addTodo(secondTodo);
        manager.removeTodo(firstTodo);
        assertEquals("second todo", todoInterface.todoList.get(0).getTask());
    }

    @Test
    public void testSortedIncompleteFirst() throws Exception {
        manager.addTodo(new Todo("Complete", true));
        manager.addTodo(new Todo("Incomplete", false));
        assertEquals(false, todoInterface.todoList.get(0).isComplete());
    }

    @Test
    public void testShowComplete() throws Exception {
        manager.addTodo(new Todo("Complete", false));
        manager.addTodo(new Todo("Incomplete", true));
        manager.showComplete();
        assertEquals(1, todoInterface.todoList.size());
        assertEquals(true, todoInterface.todoList.get(0).isComplete());
    }

    private class TestManagerInterface implements TodoManager.TodoManagerInterface {
        public List<Todo> todoList = null;

        @Override
        public void onListRefresh(List<Todo> currentList) {
            todoList = currentList;
        }
    }
}
