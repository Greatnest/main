package moomoo.task;

import moomoo.command.EditBudgetCommand;
import moomoo.command.SetBudgetCommand;
import moomoo.stubs.CategoryListStub;
import moomoo.stubs.CategoryStub;
import moomoo.stubs.ScheduleListStub;
import moomoo.stubs.UiStub;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageTest {
    @Test
    public void testBudgetFileLoad() throws MooMooException, IOException {
        File budgetFile = File.createTempFile("budget", ".txt");
        budgetFile.deleteOnExit();

        File scheduleFile = File.createTempFile("schedule", ".txt");
        scheduleFile.deleteOnExit();

        File categoryFile = File.createTempFile("category", ".txt");
        categoryFile.deleteOnExit();

        File expenditureFile = File.createTempFile("expenditure", ".txt");
        expenditureFile.deleteOnExit();

        CategoryListStub newCatList = new CategoryListStub();
        newCatList.add(null);

        ArrayList<String> categories = new ArrayList<>();

        categories.add("window");
        categories.add("sweets");
        categories.add("laptop");

        ArrayList<Double> budgets = new ArrayList<>();

        budgets.add(60.0);
        budgets.add(153.34);
        budgets.add(13840.45);

        CategoryStub newCategory = new CategoryStub();
        ScheduleListStub newCalendar = new ScheduleListStub();
        UiStub newUi = new UiStub();
        Storage newStorage = new Storage(budgetFile.getPath(), scheduleFile.getPath(), categoryFile.getPath(),
                expenditureFile.getPath());
        Budget newBudget = new Budget();

        LocalDate startDate = LocalDate.of(2017, 9, 15);
        LocalDate endDate = LocalDate.of(2019, 2, 15);

        SetBudgetCommand setBudget = new SetBudgetCommand(false, categories, budgets, startDate, endDate);
        setBudget.execute(newCalendar, newBudget, newCatList, newCategory, newUi, newStorage);

        HashMap<String, HashMap<String, Double>> newHashMap =
                newStorage.loadBudget(newCatList.getCategoryList(), newUi);

        assertEquals(60.0, newHashMap.get("01/10/2017").get("window"));
        assertEquals(153.34, newHashMap.get("01/7/2018").get("sweets"));
        assertEquals(13840.45, newHashMap.get("01/10/2018").get("laptop"));

        budgets.set(0, 500.23);
        budgets.set(2, 123.45);

        EditBudgetCommand editBudget = new EditBudgetCommand(false, categories, budgets, startDate, endDate);
        editBudget.execute(newCalendar, newBudget, newCatList, newCategory, newUi, newStorage);

        newHashMap = newStorage.loadBudget(newCatList.getCategoryList(), newUi);
        assertEquals(500.23, newHashMap.get("01/10/2017").get("window"));
        assertEquals(153.34, newHashMap.get("01/10/2018").get("sweets"));
        assertEquals(123.45, newHashMap.get("01/1/2019").get("laptop"));
    }
}