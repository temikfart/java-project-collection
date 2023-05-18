package todo;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;

public class TodoView extends Frame implements ActionListener, WindowListener {
    private final TodoModel todoModel;

    private final TextField taskField;
    private final TextField descriptionField;
    private final Choice yearChoice;
    private final Choice monthChoice;
    private final Choice dayChoice;
    private final Choice hourChoice;
    private final Choice minuteChoice;
    private final Panel scrollPanel;

    public TodoView(TodoModel todoModel) {
        super("TODO List");
        this.todoModel = todoModel;

        setLayout(new BorderLayout());
        updateSize();

        // Создаем верхнюю панель
        Panel topPanel = new Panel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(600, 60));
        topPanel.setBackground(Color.GRAY);

        // Создаем и добавляем панель с названиями полей
        Panel labelsPanel = new Panel(new GridLayout(3, 1));

        Label taskLabel = new Label("Задача:");
        Label descLabel = new Label("Описание:");
        Label deadLabel = new Label("Дедлайн:");

        labelsPanel.add(taskLabel);
        labelsPanel.add(descLabel);
        labelsPanel.add(deadLabel);

        topPanel.add(labelsPanel, BorderLayout.WEST);

        // Создаем панель с полями ввода задачи
        Panel fieldsPanel = new Panel(new GridLayout(3, 1));

        taskField = new TextField();
        descriptionField = new TextField();

        fieldsPanel.add(taskField);
        fieldsPanel.add(descriptionField);
        Panel dateTimePanel = new Panel(new GridLayout(1, 5));

        // Year choice
        this.yearChoice = new Choice();
        for (int year = 2023; year <= 2100; year++)
            yearChoice.add(String.valueOf(year));
        dateTimePanel.add(yearChoice);

        // Month choice
        this.monthChoice = new Choice();
        for (int month = 1; month <= 12; month++)
            monthChoice.add(String.format("%02d", month));
        dateTimePanel.add(monthChoice);

        // Day choice
        this.dayChoice = new Choice();
        for (int day = 1; day <= 31; day++)
            dayChoice.add(String.format("%02d", day));
        dateTimePanel.add(dayChoice);

        // Hour choice
        this.hourChoice = new Choice();
        for (int hour = 0; hour <= 23; hour++)
            hourChoice.add(String.format("%02d", hour));
        dateTimePanel.add(hourChoice);

        // Minute choice
        this.minuteChoice = new Choice();
        for (int minute = 0; minute <= 59; minute++)
            minuteChoice.add(String.format("%02d", minute));
        dateTimePanel.add(minuteChoice);

        selectCurrentDateTime();
        fieldsPanel.add(dateTimePanel);

        topPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Создаем и добавляем кнопку "Добавить"
        Button addButton = new Button("Добавить");
        addButton.setActionCommand("Add");
        addButton.addActionListener(this);

        topPanel.add(addButton, BorderLayout.EAST);

        // Добавляем верхнюю панель в окно
        add(topPanel, BorderLayout.NORTH);

        // Создаем и добавляем панель с задачами
        scrollPanel = new Panel(new GridLayout(0, 1));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(scrollPanel);
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        addWindowListener(this);
        rerender();
    }

    public void addTaskPanel(TodoTask task) {
        // Создание панели с будущей задачей
        Panel taskPanel = new Panel(new BorderLayout());
        taskPanel.setPreferredSize(new Dimension(600, 70));
        taskPanel.setBackground(Color.LIGHT_GRAY);
        taskPanel.setName(task.getId() + "");

        // Создаем панель с описанием задачи
        Panel descPanel = new Panel(new GridLayout(3, 1));
        descPanel.add(new Label("Задача: " + task.getName()));
        descPanel.add(new Label("Описание: " + task.getDescription()));
        descPanel.add(new Label(String.format("Создано: %s, Дедлайн: %s",
                task.getCreatedAt(), task.getDeadlineAt())));
        taskPanel.add(descPanel, BorderLayout.CENTER);

        // Создаем панель с кнопками управления задачи
        Panel buttonsPanel = new Panel(new GridLayout(2, 1));

        Button deleteButton = new Button("Завершить");
        deleteButton.setPreferredSize(new Dimension(100, 35));
        deleteButton.setActionCommand("Complete");
        deleteButton.addActionListener(this);
        deleteButton.setName(task.getId() + "");
        buttonsPanel.add(deleteButton);

        Button shareButton = new Button("Поделиться");
        shareButton.setPreferredSize(new Dimension(100, 35));
        shareButton.setActionCommand("Share");
        shareButton.addActionListener(this);
        shareButton.setName(task.getId() + "");
        buttonsPanel.add(shareButton);

        taskPanel.add(buttonsPanel, BorderLayout.EAST);

        scrollPanel.add(taskPanel);
        updateSize();
        validate();
    }

    public void rerender() {
        // Очищаем поля ввода
        taskField.setText("");
        descriptionField.setText("");
        selectCurrentDateTime();

        // Формируем новый пулл задач
        scrollPanel.removeAll();
        for (TodoTask task : todoModel.getTasks())
            addTaskPanel(task);

        validate();
    }

    private void updateSize() {
        if ((getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) return;
        int height = todoModel.getTasks().size() * 70 + 150;
        if (height > 500) height = 500;
        setSize(new Dimension(600, height));
    }

    private String getDateString() {
        return String.format("%s-%s-%sT%s:%s:00Z",
                yearChoice.getItem(yearChoice.getSelectedIndex()),
                monthChoice.getItem(monthChoice.getSelectedIndex()),
                dayChoice.getItem(dayChoice.getSelectedIndex()),
                hourChoice.getItem(hourChoice.getSelectedIndex()),
                minuteChoice.getItem(minuteChoice.getSelectedIndex()));
    }

    private void selectCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        yearChoice.select(currentDateTime.getYear() - 2023);
        monthChoice.select(currentDateTime.getMonthValue() - 1);
        dayChoice.select(currentDateTime.getDayOfMonth() - 1);
        hourChoice.select(currentDateTime.getHour());
        minuteChoice.select(currentDateTime.getMinute());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Button button = (Button) e.getSource();
        String cmd = button.getActionCommand();
        switch (cmd) {
            case "Add" -> TODO.todoController.addTask(taskField.getText(), descriptionField.getText(), getDateString());
            case "Complete" -> {
                int taskId = Integer.parseInt(button.getName());
                TODO.todoController.removeTask(taskId);
            }
            case "Share" -> {
                int taskId = Integer.parseInt(button.getName());
                TODO.todoController.shareTask(taskId);
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        TODO.todoController.end();
        dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
