package main.java.client;

import main.java.manager.Managers;
import main.java.manager.TaskManager;
import main.java.model.Epic;
import main.java.model.Status;
import main.java.model.Subtask;
import main.java.model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager tm = Managers.getDefault();

        // Создание задач
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

        // Просмотр задач для заполнения истории
        tm.getTaskById(t1.getId());
        tm.getTaskById(t2.getId());
        tm.getEpicById(e1.getId());
        tm.getSubtaskById(s11.getId());
        tm.getSubtaskById(s12.getId());
        tm.getEpicById(e2.getId());
        tm.getSubtaskById(s21.getId());

        System.out.println("=== Эпики ===");
        System.out.println(tm.getAllEpics());
        System.out.println("=== Задачи ===");
        System.out.println(tm.getAllTasks());
        System.out.println("=== Подзадачи ===");
        System.out.println(tm.getAllSubtasks());
        System.out.println("=== История ===");
        System.out.println(tm.getHistory());

        s11.setStatus(Status.DONE);
        tm.updateSubtask(s11);
        s21.setStatus(Status.DONE);
        tm.updateSubtask(s21);

        System.out.println("\n=== После обновления статусов ===");
        System.out.println("Эпики: " + tm.getAllEpics());
        System.out.println("Задачи: " + tm.getAllTasks());
        System.out.println("Подзадачи: " + tm.getAllSubtasks());
        System.out.println("История: " + tm.getHistory());

        tm.removeTaskById(t1.getId());
        tm.removeEpicById(e1.getId());

        System.out.println("\n=== После удалений ===");
        System.out.println("Эпики: " + tm.getAllEpics());
        System.out.println("Задачи: " + tm.getAllTasks());
        System.out.println("Подзадачи: " + tm.getAllSubtasks());
        System.out.println("История: " + tm.getHistory());

        // Демонстрация истории с ограничением в 10 элементов
        for (int i = 0; i < 15; i++) {
            tm.getTaskById(t2.getId());
        }
        System.out.println("\n=== После множественных просмотров (ограничение истории) ===");
        System.out.println("История: " + tm.getHistory());
    }
}