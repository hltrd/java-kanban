package main.java.manager;

import main.java.model.Epic;
import main.java.model.Status;
import main.java.model.Subtask;
import main.java.model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 1;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int generateId() { return nextId++; }

    // ===== Tasks =====
    @Override
    public List<Task> getAllTasks() { return new ArrayList<>(tasks.values()); }

    @Override
    public void removeAllTasks() { tasks.clear(); }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Task createTask(Task task) {
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    @Override
    public Task updateTask(Task newTask) {
        if (newTask == null) return null;
        int id = newTask.getId();
        if (!tasks.containsKey(id)) return null;
        tasks.put(id, newTask);
        return newTask;
    }

    @Override
    public boolean removeTaskById(int id) { return tasks.remove(id) != null; }

    // ===== Epics =====
    @Override
    public List<Epic> getAllEpics() { return new ArrayList<>(epics.values()); }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Epic createEpic(Epic epic) {
        int id = generateId();
        epic.setId(id);
        epic.setStatus(Status.NEW);
        epics.put(id, epic);
        return epic;
    }

    @Override
    public Epic updateEpic(Epic incoming) {
        if (incoming == null) return null;
        int id = incoming.getId();
        Epic stored = epics.get(id);
        if (stored == null) return null;
        stored.setName(incoming.getName());
        stored.setDescription(incoming.getDescription());
        return stored;
    }

    @Override
    public boolean removeEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic == null) return false;
        for (Integer sid : new ArrayList<>(epic.getSubtaskIds())) {
            subtasks.remove(sid);
        }
        return true;
    }

    // ===== Subtasks =====
    @Override
    public List<Subtask> getAllSubtasks() { return new ArrayList<>(subtasks.values()); }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask == null) return null;
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) throw new IllegalArgumentException("Эпик с id=" + epicId + " не найден");
        int id = generateId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.addSubtaskId(id);
        updateEpicStatus(epicId);
        return subtask;
    }

    @Override
    public Subtask updateSubtask(Subtask incoming) {
        if (incoming == null) return null;
        int id = incoming.getId();
        Subtask stored = subtasks.get(id);
        if (stored == null) return null;

        int oldEpicId = stored.getEpicId();
        int newEpicId = incoming.getEpicId();

        if (oldEpicId != newEpicId) {
            Epic oldEpic = epics.get(oldEpicId);
            Epic newEpic = epics.get(newEpicId);
            if (newEpic == null) throw new IllegalArgumentException("Новый эпик с id=" + newEpicId + " не найден");
            if (oldEpic != null) oldEpic.removeSubtaskId(id);
            newEpic.addSubtaskId(id);
        }

        subtasks.put(id, incoming);
        updateEpicStatus(oldEpicId);
        if (oldEpicId != newEpicId) {
            updateEpicStatus(newEpicId);
        }
        return incoming;
    }

    @Override
    public boolean removeSubtaskById(int id) {
        Subtask sub = subtasks.remove(id);
        if (sub == null) return false;
        Epic epic = epics.get(sub.getEpicId());
        if (epic != null) {
            epic.removeSubtaskId(id);
            updateEpicStatus(epic.getId());
        }
        return true;
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return Collections.emptyList();
        List<Subtask> result = new ArrayList<>();
        for (Integer id : epic.getSubtaskIds()) {
            Subtask s = subtasks.get(id);
            if (s != null) result.add(s);
        }
        return result;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // ===== Helpers =====
    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;
        List<Integer> ids = epic.getSubtaskIds();
        if (ids.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        boolean allNew = true;
        boolean allDone = true;
        for (Integer sid : ids) {
            Subtask s = subtasks.get(sid);
            if (s == null) continue;
            if (s.getStatus() != Status.NEW) allNew = false;
            if (s.getStatus() != Status.DONE) allDone = false;
        }
        if (allNew) epic.setStatus(Status.NEW);
        else if (allDone) epic.setStatus(Status.DONE);
        else epic.setStatus(Status.IN_PROGRESS);
    }
}