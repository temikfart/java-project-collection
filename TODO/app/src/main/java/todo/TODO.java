package todo;

public class TODO {
    public static final TodoController todoController = new TodoController();

    public static void main(String[] args) {
        TODO.todoController.run();
    }
}
