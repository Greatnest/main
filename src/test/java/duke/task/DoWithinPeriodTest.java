package duke.task;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoWithinPeriodTest {
    @Test
    public void testToString() {
        LocalDateTime startTime = LocalDateTime.of(2017, 2, 13, 15, 56);
        LocalDateTime endTime = LocalDateTime.of(2017, 2, 15, 16, 00);

        DoWithinPeriod newDoWithinPeriod = new DoWithinPeriod("To Complete Test", startTime, endTime);
        assertEquals("[W][NOT DONE] To Complete Test (from: 13/2/2017 1556 to: 15/2/2017 1600)", newDoWithinPeriod.toString());

        newDoWithinPeriod.markAsDone();
        assertEquals("[W][DONE] To Complete Test (from: 13/2/2017 1556 to: 15/2/2017 1600)", newDoWithinPeriod.toString());
    }
}