package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import androidx.room.Room;

import androidx.room.RoomDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;


import com.cleanup.todoc.database.TodocRoomDatabase;
import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {
    private TaskDao taskDao;
    private ProjectDao projectDao;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private TodocRoomDatabase database;
    private final Project[] projects = Project.getAllProjects();
    private final Task task1 = new Task(projects[0].getId(), "Task_Test 1", new Date().getTime());
    private final Task task2 = new Task(projects[0].getId(), "Task_Test 2", new Date().getTime());
    private final Task task3 = new Task(projects[2].getId(), "Task_Test 3", new Date().getTime());


    @Before
    public void initDatabase() throws InterruptedException {

        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                        TodocRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        this.database.projectDao().insertProjects(this.projects);

        List<Task> tasks = LiveDataTestUtils.getValue(this.database.taskDao().getTasks());
        assertTrue(tasks.isEmpty());

        /*Context context = ApplicationProvider.getApplicationContext();
        this.database = Room.inMemoryDatabaseBuilder(context,
                        TodocRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        projectDao.insertProjects(this.projects);

        List<Task> tasks = LiveDataTestUtils.getValue(taskDao.getTasks());
        assertTrue(tasks.isEmpty());*/
       /* database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        TodocRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        taskDao = database.taskDao();
        projectDao = database.projectDao();

        projectDao.insertProjects(projects);

        List<Task> tasks = LiveDataTestUtils.getValue(taskDao.getTasks());
        assertTrue(tasks.isEmpty());*/
    }


    @After
    public void closeDatabase() {
        this.database.close();
    }

    @Test
    public void insertAndGetTask() throws InterruptedException {
        List<Project> projects = LiveDataTestUtils.getValue(this.database.projectDao().getProjects());

        this.database.taskDao().insert(this.task1);
        this.database.taskDao().insert(this.task2);

        List<Task> tasks = LiveDataTestUtils.getValue(this.database.taskDao().getTasks());
        assertEquals(2, tasks.size());

        this.database.taskDao().insert(this.task3);

        tasks = LiveDataTestUtils.getValue(this.database.taskDao().getTasks());

        assertEquals(3, tasks.size());
        assertEquals(projects.get(0).getId(), tasks.get(0).getProjectId());
        assertEquals(projects.get(0).getId(), tasks.get(1).getProjectId());
        assertEquals(projects.get(2).getId(), tasks.get(2).getProjectId());

        assertEquals(task1.getName(), tasks.get(0).getName());
        assertEquals(task3.getName(), tasks.get(2).getName());
        assertEquals(task1.getCreationTimestamp(), tasks.get(0).getCreationTimestamp());
        assertEquals(task3.getCreationTimestamp(), tasks.get(2).getCreationTimestamp());
    }

    @Test
    public void deleteAndGetTask() throws InterruptedException {
        List<Project> projects = LiveDataTestUtils.getValue(this.database.projectDao().getProjects());

        this.database.taskDao().insert(this.task1);
        this.database.taskDao().insert(this.task2);
        this.database.taskDao().insert(this.task3);

        List<Task> tasks = LiveDataTestUtils.getValue(this.database.taskDao().getTasks());
        assertEquals(3, tasks.size());
        assertEquals(task2.getName(), tasks.get(1).getName());
        assertEquals(task2.getCreationTimestamp(), tasks.get(1).getCreationTimestamp());

        this.database.taskDao().delete(tasks.get(1));
        tasks = LiveDataTestUtils.getValue(this.database.taskDao().getTasks());
        assertEquals(2, tasks.size());
        assertEquals(task3.getName(), tasks.get(1).getName());
        assertEquals(task3.getCreationTimestamp(), tasks.get(1).getCreationTimestamp());
    }

}
