package duke.command;

import duke.task.*;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Represents the command to list tasks within deadlines specified by user.
 */
public class ReminderCommand extends Command {
    /**
     * Takes in a flag to represent if it should exit and the input given by the User.
     * @param isExit True if the program should exit after running this command, false otherwise
     * @param input Input given by the user
     */
    public ReminderCommand(boolean isExit, String input) {
        super(isExit, input);
    }

    /**
     * Outputs events if timing occurs within input and the current timing.
     * @param taskList Task List containing the initialized lists of the task on run
     * @param ui Ui for which any input and output will be given to
     * @param storage Storage for storing and writing of the data to disk
     * @throws DukeException thrown if input is invalid
     */
    public void execute(TaskList taskList, Ui ui, Storage storage) throws DukeException {
        if (input.length() < 10) {
            throw new DukeException("OOPS!!! The period cannot be empty.");
        }

        String period = input.substring(9);

        if (taskList.getSize() == 0) {
            throw new DukeException("You have no tasks in your list");
        }

        int start = 1;
        String outputString = "";
        String durationString = "P";
        String[] toParseArray = period.split(" ");
        boolean exists = false;


        int dayValue = searchArray(toParseArray, "DAYS|DAY");
        int hrValue = searchArray(toParseArray, "HRS|HR");
        int minValue = searchArray(toParseArray, "MINS|MIN");
        int secValue = searchArray(toParseArray, "SECS|SEC");

        if (dayValue != -1) {
            durationString += toParseArray[dayValue-1] + "D";
        }

        if (hrValue != -1) {
            durationString += "T" + toParseArray[hrValue-1] + "H";
        }

        if (minValue != -1) {
            if (durationString.contains("T")) {
                durationString += toParseArray[minValue-1] + "M";
            } else {
                durationString += "T" + toParseArray[minValue-1] + "M";
            }
        }

        if (secValue!=-1) {
            if (durationString.contains("T")) {
                durationString += toParseArray[secValue-1] + "S";
            } else {
                durationString += "T" + toParseArray[secValue-1] + "S";
            }
        }

        if (durationString.equals("P")) {
            throw new DukeException("Please enter a duration in this format \"2 HRS 3 HRS 1 MIN 12 SECS\"");
        }
        Duration newDuration = Duration.parse(durationString);
        outputString += "Here are the tasks in your list occurring within " + period + ":\n" ;

        for (int i = 0; i < taskList.getSize(); ++i) {
            Task value = taskList.getTask(i);
            if (value instanceof Deadline) {
                Duration dur = Duration.between(LocalDateTime.now(), ((Deadline) value).getBy());
                if (dur.compareTo(newDuration) < 0) {
                    outputString += (i + 1) + "." + value.toString() + "\n";
                    exists = true;
                }

            } else if (value instanceof Event) {
                Duration dur = Duration.between(LocalDateTime.now(), ((Event) value).getAt());
                if (dur.compareTo(newDuration) < 0) {
                    outputString += (i + 1) + "." + value.toString() + "\n";
                    exists = true;
                }
            }
        }

        if (exists) {
            outputString = outputString.substring(0, outputString.length() - 1);
            ui.output = outputString;
        } else {
            ui.output = "There are no tasks in your list occuring within " + period;
        }
    }
    private int searchArray(String[] toFindArray, String toFindValue) throws DukeException{
        String[] splitArray = toFindValue.split("\\|");
        int i = 0;
        for (i = 0; i < toFindArray.length; ++i) {
            if (toFindArray[i].equals(splitArray[0]) || toFindArray[i].equals(splitArray[1])) {
                try {
                    Integer.parseInt(toFindArray[i-1]);
                } catch(Exception e) {
                    throw new DukeException("Please enter a duration in this format \"2 HRS 3 HRS 5 MINS 12 SEC\"");
                }
                return i;
            }
        }
        return -1;
    }

}
