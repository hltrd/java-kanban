package test.model;

import main.java.model.Epic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void testEpicEqualityById() {
        Epic epic1 = new Epic("Test", "Description");
        Epic epic2 = new Epic("Test2", "Description2");
        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2, "Эпики с одинаковым id должны быть равны");
    }

    @Test
    void testEpicSubtaskManagement() {
        Epic epic = new Epic("Test", "Description");
        epic.setId(1);

        epic.addSubtaskId(10);
        epic.addSubtaskId(20);

        assertEquals(2, epic.getSubtaskIds().size(), "Эпик должен содержать 2 подзадачи");
        assertTrue(epic.getSubtaskIds().contains(10), "Эпик должен содержать подзадачу с id=10");
        assertTrue(epic.getSubtaskIds().contains(20), "Эпик должен содержать подзадачу с id=20");

        epic.removeSubtaskId(10);
        assertEquals(1, epic.getSubtaskIds().size(), "Эпик должен содержать 1 подзадачу после удаления");

        epic.clearSubtasks();
        assertEquals(0, epic.getSubtaskIds().size(), "Эпик не должен содержать подзадачи после очистки");
    }
}