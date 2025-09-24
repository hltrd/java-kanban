package test.manager;

import main.java.manager.HistoryManager;
import main.java.manager.Managers;
import main.java.model.Task;
import main.java.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void testAddTaskToHistory() {
        Task task = new Task("Test", "Description", Status.NEW);
        task.setId(1);

        historyManager.add(task);
        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size(), "История должна содержать 1 задачу");
        assertEquals(task, history.get(0), "Задача в истории должна совпадать с добавленной");
    }

    @Test
    void testHistoryPreservesTaskData() {
        Task task = new Task("Test", "Description", Status.NEW);
        task.setId(1);

        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        Task historicalTask = history.get(0);

        assertEquals(task.getId(), historicalTask.getId(), "Id должно сохраняться в истории");
        assertEquals(task.getName(), historicalTask.getName(), "Название должно сохраняться в истории");
        assertEquals(task.getDescription(), historicalTask.getDescription(), "Описание должно сохраняться в истории");
        assertEquals(task.getStatus(), historicalTask.getStatus(), "Статус должен сохраняться в истории");
    }

    @Test
    void testHistoryLimit() {
        for (int i = 0; i < 15; i++) {
            Task task = new Task("Task " + i, "Description", Status.NEW);
            task.setId(i);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size(), "История должна быть ограничена 10 элементами");

        // Проверяем, что остались последние 10 задач
        for (int i = 5; i < 15; i++) {
            Task expected = new Task("Task " + i, "Description", Status.NEW);
            expected.setId(i);
            assertTrue(history.contains(expected), "История должна содержать задачу с id=" + i);
        }
    }
}