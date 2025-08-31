import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public List<Integer> getSubtaskIds() {
        return Collections.unmodifiableList(subtaskIds);
    }

    void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    void removeSubtaskId(int subtaskId) {
        subtaskIds.remove((Integer) subtaskId);
    }

    void clearSubtasks() {
        subtaskIds.clear();
    }

    @Override
    public String toString() {
        return String.format("Epic{id=%d, name='%s', status=%s, subtasks=%s}",
                getId(), getName(), getStatus(), subtaskIds);
    }
}
