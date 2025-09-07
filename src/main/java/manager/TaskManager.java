package main.java.manager;

import main.java.model.Epic;
import main.java.model.Subtask;
import main.java.model.Task;

import java.util.List;

public interface TaskManager {
    // Tasks
    List<Task> getAllTasks();
    void removeAllTasks();
    Task getTaskById(int id);
    Task createTask(Task task);
    Task updateTask(Task task);
    boolean removeTaskById(int id);

    // Epics
    List<Epic> getAllEpics();
    void removeAllEpics();
    Epic getEpicById(int id);
    Epic createEpic(Epic epic);
    Epic updateEpic(Epic epic);
    boolean removeEpicById(int id);

    // Subtasks
    List<Subtask> getAllSubtasks();
    void removeAllSubtasks();
    Subtask getSubtaskById(int id);
    Subtask createSubtask(Subtask subtask);
    Subtask updateSubtask(Subtask subtask);
    boolean removeSubtaskById(int id);
    List<Subtask> getSubtasksOfEpic(int epicId);

    // History
    List<Task> getHistory();
}