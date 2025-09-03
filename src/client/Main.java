package client;

import manager.TaskManager;
import model.*;

public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();

        Task t1 = new Task("Переезд", "Собрать коробки", Status.NEW);
        Task t2 = new Task("Сделать отчёт", "Отправить до пятницы", Status.IN_PROGRESS);
        tm.createTask(t1);
        tm.createTask(t2);

        Epic e1 = new Epic("Написать курсовую", "Сдать до конца семестра");
        tm.createEpic(e1);
        Subtask s11 = new Subtask("Теория", "Глава 1-3", e1.getId(), Status.NEW);
        Subtask s12 = new Subtask("Практика", "Программный прототип", e1.getId(), Status.IN_PROGRESS);
        tm.createSubtask(s11);
        tm.createSubtask(s12);

        Epic e2 = new Epic("Подготовить выступление", "Митап в субботу");
        tm.createEpic(e2);
        Subtask s21 = new Subtask("Слайды", "20 слайдов", e2.getId(), Status.NEW);
        tm.createSubtask(s21);

        System.out.println("=== Эпики ===");
        System.out.println(tm.getAllEpics());
        System.out.println("=== Задачи ===");
        System.out.println(tm.getAllTasks());
        System.out.println("=== Подзадачи ===");
        System.out.println(tm.getAllSubtasks());

        s11.setStatus(Status.DONE);
        tm.updateSubtask(s11);
        s21.setStatus(Status.DONE);
        tm.updateSubtask(s21);

        System.out.println("\n=== После обновления статусов ===");
        System.out.println("Эпики: " + tm.getAllEpics());
        System.out.println("Задачи: " + tm.getAllTasks());
        System.out.println("Подзадачи: " + tm.getAllSubtasks());

        tm.removeTaskById(t1.getId());
        tm.removeEpicById(e1.getId());

        System.out.println("\n=== После удалений ===");
        System.out.println("Эпики: " + tm.getAllEpics());
        System.out.println("Задачи: " + tm.getAllTasks());
        System.out.println("Подзадачи: " + tm.getAllSubtasks());
    }
}

