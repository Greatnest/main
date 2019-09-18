package duke.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class that represents a deadline task that allows for a deadline to be set
 * Subclass of the Task Class.
 */
public class Reminder extends Task {

    protected Duration period;

    /**
     * Constructor that takes in a description of the task and the deadline of the task.
     * @param description A String representing the task to be completed
     * @param period A LocalDateTime representing the deadline of the task.
     */
    public Reminder(String description, Duration period) {
        super(description);
        this.period = period;
    }

    @Override
    public String toString() {
        String days = String.format("%d Days ", period.toDaysPart());
        String hms = String.format("%d:%02d:%02d",
                period.toHoursPart(),
                period.toMinutesPart(),
                period.toSecondsPart());
        return "[F]" + super.toString() + " (for: " + days + hms + ")";
    }

}