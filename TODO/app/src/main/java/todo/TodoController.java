package todo;

import java.io.IOException;
import java.net.URL;

public class TodoController {
    private final TodoView todoView;
    private final TodoModel todoModel;

    public TodoController() {
        this.todoModel = new TodoModel();
        this.todoView = new TodoView(todoModel);
    }

    public void run() {
        todoView.setVisible(true);
    }

    public void end() {
        todoModel.saveTasks();
    }

    public void addTask(String name, String desc, String deadline) {
        todoModel.addTask(new TodoTask(name, desc, deadline));
        todoView.rerender();
    }

    public void removeTask(int id) {
        todoModel.removeTask(id);
        todoView.rerender();
    }

    public void shareTask(int id) {
        TodoTask task = todoModel.getTask(id);
        if (task != null) {
            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
            String apiToken = "";
            String chatId = "";
            urlString = String.format(urlString, apiToken, chatId, task.toMessage());
            try {
                URL url = new URL(urlString);
                url.openConnection().getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
