package test.model;

import main.java.model.Status;
import main.java.model.Subtask;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void testSubtaskEqualityById() {
        Subtask subtask1 = new Subtask("Test", "Description", 1, Status.NEW);
        Subtask subtask2 = new Subtask("Test2", "Description2", 2, Status.DONE);
        subtask1.setId(1);
        subtask2.setId(1);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым id должны быть равны");
    }

    @Test
    void testSubtaskEpicIdManagement() {
        Subtask subtask = new Subtask("Test", "Description", 1, Status.NEW);
        subtask.setId(10);

        assertEquals(1, subtask.getEpicId(), "Изначальный epicId должен быть 1");

        subtask.setEpicId(2);
        assertEquals(2, subtask.getEpicId(), "epicId должен измениться на 2");
    }
}