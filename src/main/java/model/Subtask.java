package main.java.model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicId, Status status) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, int epicId, Status status) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() { return epicId; }
    public void setEpicId(int epicId) { this.epicId = epicId; }

    @Override
    public String toString() {
        return String.format("Subtask{id=%d, epicId=%d, name='%s', status=%s}",
                getId(), epicId, getName(), getStatus());
    }
}