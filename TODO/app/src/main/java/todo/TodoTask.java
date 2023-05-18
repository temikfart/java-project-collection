package todo;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TodoTask implements Serializable {
    private static int idCounter = 0;

    private final int taskId = next();
    private final String name;
    private final String description;
    private final Instant createdAt;
    private final Instant deadlineAt;

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy, dd LLLL HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    public TodoTask(String name, String description, String deadlineAt) {
        this.name = name;
        this.description = description;
        this.createdAt = Instant.now();
        int zoneOffsetInSeconds = ZoneId.systemDefault().getRules().getOffset(createdAt).getTotalSeconds();
        this.deadlineAt = Instant.parse(deadlineAt).minus(Duration.ofSeconds(zoneOffsetInSeconds));
    }

    private static int next() {
        return idCounter++;
    }

    public int getId() {
        return taskId;
    }

    public String getName() {
        return name.isEmpty() ? "<Задача>" : name;
    }

    public String getDescription() {
        return description.isEmpty() ? "<Описание>" : description;
    }

    public String getCreatedAt() {
        return formatter.format(createdAt);
    }

    public String getDeadlineAt() {
        return formatter.format(deadlineAt);
    }

    public String toMessage() {
        return String.format("%s%%0A---%%0A%s%%0A---%%0AСоздано: %s%%0AДедлайн: %s",
                name, description, formatter.format(createdAt), formatter.format(deadlineAt));
    }
}
