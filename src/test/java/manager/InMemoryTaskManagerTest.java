package test.manager;

import main.java.manager.Managers;
import main.java.manager.TaskManager;
import main.java.model.Epic;
import main.java.model.Status;
import main.java.model.Subtask;
import main.java.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void testAddAndFindDifferentTaskTypes() {
        Task task = new Task("Task", "Description", Status.NEW);
        taskManager.createTask(task);

        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Description", epic.getId(), Status.NEW);
        taskManager.createSubtask(subtask);

        assertNotNull(taskManager.getTaskById(task.getId()), "Задача должна находиться по id");
        assertNotNull(taskManager.getEpicById(epic.getId()), "Эпик должен находиться по id");
        assertNotNull(taskManager.getSubtaskById(subtask.getId()), "Подзадача должна находиться по id");
    }

    @Test
    void testEpicCannotBeSubtaskToItself() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test", "Description", epic.getId(), Status.NEW);
        subtask.setEpicId(epic.getId()); // Это нормально

        // Не должно быть возможности установить эпику самого себя в качестве подзадачи
        assertNotEquals(epic.getId(), subtask.getId(), "Эпик не может быть подзадачей самому себе");
    }

    @Test
    void testSubtaskCannotBeItsOwnEpic() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test", "Description", epic.getId(), Status.NEW);
        // Не должно быть возможности установить подзадаче её же id в качестве epicId
        assertNotEquals(subtask.getId(), subtask.getEpicId(), "Подзадача не может быть своим же эпиком");
    }

    @Test
    void testHistoryAfterTaskViews() {
        Task task = new Task("Task", "Description", Status.NEW);
        taskManager.createTask(task);

        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Description", epic.getId(), Status.NEW);
        taskManager.createSubtask(subtask);

        // Просмотр задач
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(3, history.size(), "История должна содержать 3 задачи");
        assertTrue(history.contains(task), "История должна содержать задачу");
        assertTrue(history.contains(epic), "История должна содержать эпик");
        assertTrue(history.contains(subtask), "История должна содержать подзадачу");
    }

    @Test
    void testHistoryLimit() {
        for (int i = 0; i < 15; i++) {
            Task task = new Task("Task " + i, "Description", Status.NEW);
            taskManager.createTask(task);
            taskManager.getTaskById(task.getId());
        }

        List<Task> history = taskManager.getHistory();
        assertEquals(10, history.size(), "История должна быть ограничена 10 элементами");
    }
}