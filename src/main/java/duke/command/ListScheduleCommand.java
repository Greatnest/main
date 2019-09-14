package duke.command;
import duke.task.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Class that represents command for the tasks to be listed.
 */
public class ListScheduleCommand extends Command{
    /**
     * Constructor that takes in a flag to represent if it should exit and the input given by the User
     * @param isExit True if the program should exit after running this command, false otherwise
     * @param input Input given by the user
     */
    public ListScheduleCommand(Boolean isExit, String input) {
        super(isExit, input);
    }

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws DukeException {
        ui.output = "";
        if (input.length() < 10) {
            throw new DukeException("OOPS!!! The schedule time and date cannot be empty.");
        }
        input = input.substring(9);
        LocalDate inputDate;
        try {
            inputDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("d/M/yyyy"));
        } catch(DateTimeParseException e) {
            throw new DukeException("OOPS!!! Please format your date in this format \"1/12/2019\"");
        }
        for (int i = 0; i < taskList.getSize(); ++i) {
            LocalDate eventDate;
            Task currentTask = taskList.getTask(i);
            if (currentTask instanceof Event) {
                eventDate = ((Event) currentTask).getAt().toLocalDate();
            } else if (currentTask instanceof Deadline) {
                eventDate = ((Deadline) currentTask).getBy().toLocalDate();
            } else {
                continue;
            }

            if (eventDate.equals(inputDate)) {
                if (ui.output == "") {
                    ui.output = "These are the tasks on " + input + ":\n";
                }
                ui.output += Integer.toString(i+1) + "." + currentTask.toString() + "\n";
            }
        }
        if (ui.output == "") {
            ui.output = "There are no tasks on " + input + "\n";
        }

    }
}
