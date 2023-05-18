package todo;

import java.io.*;
import java.util.ArrayList;

public class TodoModel {
    private final File tasksFile;
    private final ArrayList<TodoTask> taskList;

    public TodoModel() {
        this.tasksFile = new File("tasks.db");
        try {
            if (!tasksFile.exists())
                if (!tasksFile.createNewFile())
                    System.out.println("Can't create new tasks storage file");
            this.taskList = readTasks();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + tasksFile.getName() + " is not found: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Can't create i/o stream with tasks storage file: " + e.getMessage());
        }
    }

    public ArrayList<TodoTask> getTasks() {
        return taskList;
    }

    public void addTask(TodoTask task) {
        taskList.add(task);
    }

    public void removeTask(int id) {
        for (TodoTask task : taskList) {
            if (task.getId() == id) {
                taskList.remove(task);
                break;
            }
        }
    }

    public TodoTask getTask(int id) {
        for (TodoTask task : taskList)
            if (task.getId() == id)
                return task;
        return null;
    }

    public void saveTasks() {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(tasksFile))) {
            for (TodoTask task : taskList)
                output.writeObject(task);
        } catch (IOException e) {
            throw new RuntimeException("Can't write into tasks storage file: " + e.getMessage());
        }
    }

    private ArrayList<TodoTask> readTasks() {
        ArrayList<TodoTask> taskList = new ArrayList<>();
        try {
            FileInputStream fileInput = new FileInputStream(tasksFile);
            if (fileInput.available() == 0)
                return new ArrayList<>();
            ObjectInputStream input = new ObjectInputStream(fileInput);
            try {
                for (; ; )
                    taskList.add((TodoTask) input.readObject());
            } catch (EOFException e) {
                System.out.println("EOF");
                input.close();
                fileInput.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't read from tasks storage file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        return taskList;
    }
}
