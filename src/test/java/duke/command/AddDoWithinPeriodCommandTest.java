package duke.command;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import duke.task.DukeException;
import duke.task.TaskList;
import duke.task.Storage;
import duke.task.Ui;

public class AddDoWithinPeriodCommandTest {
    @Test
    public void testDeadlineCommand() throws DukeException, IOException {
        File tempFile = File.createTempFile("duke",".txt");
        tempFile.deleteOnExit();

        TaskList newTaskList = new TaskList();
        Ui newUi = new Ui();
        Storage newStorage = new Storage(tempFile.getPath());

        AddDoWithinPeriodCommand doWithinPeriodCommand = new AddDoWithinPeriodCommand(false,"dowithin To complete work /from 1/1/2019 1830 /to 3/2/2019 1900");
        doWithinPeriodCommand.execute(newTaskList, newUi, newStorage);

        assertEquals(1, newTaskList.getSize());
        assertTrue(tempFile.exists());
        assertEquals( "W | 0 | To complete work | 1/1/2019 1830 | 3/2/2019 1900", Files.readAllLines(Paths.get(tempFile.getPath())).get(0));
    }
}