package com.cleanup.todoc.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.TodocRoomDatabase;
import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class TaskRepository {
    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;
    private final LiveData<List<Project>> allProjects;

    public TaskRepository(Application application) {
        TodocRoomDatabase database = TodocRoomDatabase.getDatabase(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getTasks();
        ProjectDao projectDao = database.projectDao();
        allProjects = projectDao.getProjects();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<Project>> getAllProjects() {
        return allProjects;
    }

    public void insert(Task task) {
        TodocRoomDatabase.databaseWriteExecutor.execute(() -> taskDao.insert(task));
    }

    public void delete(Task task) {
        TodocRoomDatabase.databaseWriteExecutor.execute(() -> taskDao.delete(task));
    }
}
