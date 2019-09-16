package duke.command;

import duke.task.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents the command for a task to postponed
 * Subclass of Command
 */
public class PostponeTaskCommand extends Command {
    /**
     * Takes in a flag to represent if it should exit and the input given by the User
     * @param isExit True if the program should exit after running this command, false otherwise
     * @param input Input given by the user
     */
    public PostponeTaskCommand(boolean isExit, String input) {
        super(isExit, input);
    }

    public void execute(TaskList taskList, Ui ui, Storage storage) throws DukeException {
        if (input.length() < 10) {
            throw new DukeException("OOPS!!! The task to postpone cannot be empty.");
        }

        input = input.substring(9);

        int dateIndex = input.indexOf("/to ");
        if (dateIndex == -1) {
            throw new DukeException("OOPS!!! Please indicate the new timing after \"/to\"");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(input.substring(0, dateIndex-1));
        } catch (NumberFormatException e) {
            throw new DukeException("Please enter a task number.");
        }
        LocalDateTime newTiming = parseDate(input.substring(dateIndex+4));

        Task toPostpone = taskList.getTask(taskNumber-1);
        if (toPostpone instanceof ToDo) {
            throw new DukeException("The task you selected has no deadline or timing.");
        } else if (toPostpone instanceof Event) {
           ((Event) toPostpone).setNewTiming(newTiming);
        } else if (toPostpone instanceof Deadline) {
            ((Deadline) toPostpone).setNewTiming(newTiming);
        }
        ui.output = "Here is the task that you have postponed:\n" + toPostpone.toString();
        storage.saveToFile();
    }

    /**
     * Used to convert a string given to an appropriate LocalDateTime Object
     * @param dateToParse String to be converted
     * @return LocalDateTime object in d/M/yyyy HHmm format (2/2/2019 1830)
     * @throws DukeException Thrown if the input given does not match the format
     */
    private LocalDateTime parseDate(String dateToParse) throws DukeException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
            return LocalDateTime.parse(dateToParse, formatter);
        } catch (DateTimeParseException e) {
            throw new DukeException("OOPS!!! Please format your date and time in this format \"20/12/2019 1859\"");
        }
    }

}
