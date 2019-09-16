package duke.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class that represents an Event Task with a timing of the event.
 * Subclass of the Task Class
 */
public class Event extends Task {

    protected LocalDateTime at;

    /**
     * Constructor that takes in the event that will happen and the start time of the event
     * @param description A String representing the event taking place
     * @param at LocalDateTime object representing the start time of the event
     */
    public Event(String description, LocalDateTime at) {
        super(description);
        this.at = at;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (at: " + dateToString(at) + ")";
    }

    /**
     * Sets the new timing.
     * @param inputTiming New LocalDateTime to be set
     */
    public void setNewTiming(LocalDateTime inputTiming) {
        this.at = inputTiming;
    }

    /**
     * Converts the input LocalDateTime to printable format in String
     * @param dateTime LocalDateTime object to be converted to String
     * @return String format of the LocalDateTime
     */
    private String dateToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        return dateTime.format(formatter);
    }

}