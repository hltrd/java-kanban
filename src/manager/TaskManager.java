package manager;

import model.*;

import java.util.*;

public class TaskManager {
    private int nextId = 1;

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private int generateId() { return nextId++; }

    // ===== Tasks =====
    public List<Task> getAllTasks() { return new ArrayList<>(tasks.values()); }
    public void removeAllTasks() { tasks.clear(); }
    public Task getTaskById(int id) { return tasks.get(id); }

    public Task createTask(Task task) {
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    public Task updateTask(Task newTask) {
        if (newTask == null) return null;
        int id = newTask.getId();
        if (!tasks.containsKey(id)) return null;
        tasks.put(id, newTask);
        return newTask;
    }

    public boolean removeTaskById(int id) { return tasks.remove(id) != null; }

    // ===== Epics =====
    public List<Epic> getAllEpics() { return new ArrayList<>(epics.values()); }
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }
    public Epic getEpicById(int id) { return epics.get(id); }

    public Epic createEpic(Epic epic) {
        int id = generateId();
        epic.setId(id);
        epics.put(id, epic);
        return epic;
    }

    public Epic updateEpic(Epic incoming) {
        if (incoming == null) return null;
        int id = incoming.getId();
        Epic stored = epics.get(id);
        if (stored == null) return null;
        stored.setName(incoming.getName());
        stored.setDescription(incoming.getDescription());
        return stored;
    }

    public boolean removeEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic == null) return false;
        for (Integer sid : new ArrayList<>(epic.getSubtaskIds())) {
            subtasks.remove(sid);
        }
        return true;
    }

    // ===== Subtasks =====
    public List<Subtask> getAllSubtasks() { return new ArrayList<>(subtasks.values()); }
    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
        }
    }
    public Subtask getSubtaskById(int id) { return subtasks.get(id); }

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
        updateEpicStatus(newEpicId);
        return incoming;
    }

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
