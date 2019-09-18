package duke.command;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import duke.task.DukeException;
import duke.task.TaskList;
import duke.task.Storage;
import duke.task.Ui;

public class ReminderCommandTest {
    /**
     * Tests have to be changed based on the current's day timing.
     * @throws DukeException
     * @throws IOException
     */
    @Test
    public void testReminderCommand() throws DukeException, IOException {
        TaskList newTaskList = new TaskList();
        Ui newUi = new Ui();
        Storage newStorage = new Storage("");

        AddEventCommand eventCommand = new AddEventCommand(false,"event Birthday Party /at 19/9/2019 1830");
        eventCommand.execute(newTaskList, newUi, newStorage);

        AddDeadLineCommand deadLineCommand = new AddDeadLineCommand(false,"deadline do homework /by 20/9/2019 1830");
        deadLineCommand.execute(newTaskList, newUi, newStorage);

        ReminderCommand newReminderCommand = new ReminderCommand(false, "reminder 1 DAY 23 HRS");
        newReminderCommand.execute(newTaskList, newUi, newStorage);

        assertEquals("Here are the tasks in your list occurring within 1 DAY 23 HRS:\n" +
                "1.[E][NOT DONE] Birthday Party (at: 19/9/2019 1830)\n" +
                "2.[D][NOT DONE] do homework (by: 20/9/2019 1830)", newUi.output);

        newReminderCommand = new ReminderCommand(false, "reminder 1 DAY 18 HRS");
        newReminderCommand.execute(newTaskList, newUi, newStorage);

        assertEquals("Here are the tasks in your list occurring within 1 DAY 18 HRS:\n" +
                "1.[E][NOT DONE] Birthday Party (at: 19/9/2019 1830)", newUi.output);
    }
}