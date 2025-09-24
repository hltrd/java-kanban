package test.model;

import main.java.model.Status;
import main.java.model.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskEqualityById() {
        Task task1 = new Task("Test", "Description", Status.NEW);
        Task task2 = new Task("Test2", "Description2", Status.IN_PROGRESS);
        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }

    @Test
    void testTaskHashCode() {
        Task task1 = new Task("Test", "Description", Status.NEW);
        Task task2 = new Task("Test2", "Description2", Status.IN_PROGRESS);
        task1.setId(1);
        task2.setId(1);

        assertEquals(task1.hashCode(), task2.hashCode(), "Хэш-коды задач с одинаковым id должны быть равны");
    }

    @Test
    void testTaskImmutabilityWhenAdded() {
        Task original = new Task("Original", "Description", Status.NEW);
        int originalId = original.getId();
        String originalName = original.getName();
        String originalDescription = original.getDescription();
        Status originalStatus = original.getStatus();

        // Симуляция "сохранения" задачи
        Task saved = new Task(originalId, originalName, originalDescription, originalStatus);

        assertEquals(originalId, saved.getId(), "Id не должно изменяться");
        assertEquals(originalName, saved.getName(), "Название не должно изменяться");
        assertEquals(originalDescription, saved.getDescription(), "Описание не должно изменяться");
        assertEquals(originalStatus, saved.getStatus(), "Статус не должен изменяться");
    }
}