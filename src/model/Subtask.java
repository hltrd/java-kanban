package model;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, int epicId, Status status) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, int epicId, Status status) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() { return epicId; }

    @Override
    public String toString() {
        return String.format("model.Subtask{id=%d, epicId=%d, name='%s', status=%s}",
                getId(), epicId, getName(), getStatus());
    }
}
