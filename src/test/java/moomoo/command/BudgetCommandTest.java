package moomoo.command;

import moomoo.stubs.CategoryStub;
import moomoo.stubs.CategoryListStub;
import moomoo.stubs.ScheduleListStub;
import moomoo.stubs.StorageStub;
import moomoo.stubs.UiStub;
import moomoo.task.Budget;
import moomoo.task.MooMooException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BudgetCommandTest {
    @Test
    public void testSetBudgetCommand() throws MooMooException, IOException {
        File budgetFile = File.createTempFile("budget", ".txt");
        budgetFile.deleteOnExit();

        File scheduleFile = File.createTempFile("schedule", ".txt");
        scheduleFile.deleteOnExit();

        File expenditureFile = File.createTempFile("expenditure", ".txt");
        expenditureFile.deleteOnExit();

        CategoryListStub newCatList = new CategoryListStub();

        newCatList.add(null);

        ArrayList<String> categories = new ArrayList<>();

        categories.add("Shoes");
        categories.add("Food");
        categories.add("Places to go");
        categories.add("Test Value");

        ArrayList<Double> budgets = new ArrayList<>();

        budgets.add(50.0);
        budgets.add(100.0);
        budgets.add(-150.0);
        budgets.add(200.0);


        CategoryStub newCategory = new CategoryStub();
        ScheduleListStub newCalendar = new ScheduleListStub();
        UiStub newUi = new UiStub();
        StorageStub newStorage = new StorageStub();
        Budget newBudget = new Budget();

        LocalDate startDate = LocalDate.of(2017, 9, 15);
        LocalDate endDate = LocalDate.of(2019, 2, 15);

        SetBudgetCommand setBudget = new SetBudgetCommand(false, categories, budgets, startDate, endDate);
        setBudget.execute(newCalendar, newBudget, newCatList, newCategory, newUi, newStorage);

        assertEquals("You have set $50.00 as the budget for shoes from SEPTEMBER 2017 to FEBRUARY 2019."
                + " If the budget has already been set, no changes will be done.\n"
                + "You have set $100.00 as the budget for food from SEPTEMBER 2017 to FEBRUARY 2019. "
                + "If the budget has already been set, no changes will be done.\n"
                + "Please set your budget for places to go to a value more than 0\n"
                + "test value category does not exist. Please add it first.\n", newUi.returnResponse());
    }

    @Test
    public void testEditBudgetCommand() throws MooMooException, IOException  {
        File budgetFile = File.createTempFile("budget", ".txt");
        budgetFile.deleteOnExit();

        File scheduleFile = File.createTempFile("schedule", ".txt");
        scheduleFile.deleteOnExit();

        File expenditureFile = File.createTempFile("expenditure", ".txt");
        expenditureFile.deleteOnExit();

        CategoryListStub newCatList = new CategoryListStub();
        newCatList.add(null);

        ArrayList<String> categories = new ArrayList<>();

        categories.add("shoes");
        categories.add("window");
        categories.add("sweets");
        categories.add("Invalid Value");

        ArrayList<Double> budgets = new ArrayList<>();

        budgets.add(123.45);
        budgets.add(1123.0);
        budgets.add(217.0);
        budgets.add(122.23);

        CategoryStub newCategory = new CategoryStub();
        ScheduleListStub newCalendar = new ScheduleListStub();
        UiStub newUi = new UiStub();
        StorageStub newStorage = new StorageStub();
        Budget newBudget = new Budget();

        LocalDate startDate = LocalDate.of(2017, 9, 15);
        LocalDate endDate = LocalDate.of(2019, 2, 15);

        SetBudgetCommand setBudget = new SetBudgetCommand(false, categories, budgets, startDate, endDate);
        setBudget.execute(newCalendar, newBudget, newCatList, newCategory, newUi, newStorage);

        budgets.set(0, 300.0);
        budgets.set(1, -675.0);
        budgets.set(2, 675.0);

        EditBudgetCommand editBudget = new EditBudgetCommand(false, categories, budgets, startDate, endDate);
        editBudget.execute(newCalendar, newBudget, newCatList, newCategory, newUi, newStorage);

        assertEquals("You have changed the budget for shoes to $300.00 from SEPTEMBER 2017 to FEBRUARY 2019. "
                + "Please view the changed budget using budget list.\n"
                + "Please set your budget for window to a value more than 0\n"
                + "You have changed the budget for sweets to $675.00 from SEPTEMBER 2017 to FEBRUARY 2019. "
                + "Please view the changed budget using budget list.\n"
                + "invalid value category does not exist. Please add it first.\n", newUi.returnResponse());
    }

    @Test
    public void testListBudgetCommand() throws MooMooException, IOException  {
        File budgetFile = File.createTempFile("budget", ".txt");
        budgetFile.deleteOnExit();

        File scheduleFile = File.createTempFile("schedule", ".txt");
        scheduleFile.deleteOnExit();

        File expenditureFile = File.createTempFile("expenditure", ".txt");
        expenditureFile.deleteOnExit();

        CategoryListStub newCatList = new CategoryListStub();
        newCatList.add(null);

        ArrayList<String> categories = new ArrayList<>();

        categories.add("shoes");
        categories.add("food");
        categories.add("places to go");
        categories.add("Test Value");
        categories.add("sweets");
        ArrayList<Double> budgets = new ArrayList<>();

        budgets.add(183.0);
        budgets.add(128.0);
        budgets.add(527.0);
        budgets.add(103.34);
        budgets.add(105.0);

        CategoryStub newCategory = new CategoryStub();
        ScheduleListStub newCalendar = new ScheduleListStub();
        UiStub newUi = new UiStub();
        StorageStub newStorage = new StorageStub();

        Budget newBudget = new Budget();

        LocalDate startDate = LocalDate.of(2018, 12, 15);
        LocalDate endDate = LocalDate.of(2019, 2, 15);

        SetBudgetCommand setBudget = new SetBudgetCommand(false, categories, budgets, startDate, endDate);
        setBudget.execute(newCalendar, newBudget, newCatList, newCategory, newUi, newStorage);

        ArrayList<String> listCategories = new ArrayList<>();

        listCategories.add("shoes");
        listCategories.add("food");
        listCategories.add("places to go");
        listCategories.add("Test Value");
        listCategories.add("sweets");

        ListBudgetCommand listBudget = new ListBudgetCommand(false, listCategories, startDate, endDate);
        listBudget.execute(newCalendar, newBudget, newCatList, newCategory, newUi, newStorage);

        assertEquals("Budget for shoes for 12/2018 is $183.00\n"
                + "Budget for shoes for 1/2019 is $183.00\n"
                + "Budget for shoes for 2/2019 is $183.00\n"
                + "Budget for food for 12/2018 is $128.00\n"
                + "Budget for food for 1/2019 is $128.00\n"
                + "Budget for food for 2/2019 is $128.00\n"
                + "Budget for places to go for 12/2018 is $527.00\n"
                + "Budget for places to go for 1/2019 is $527.00\n"
                + "Budget for places to go for 2/2019 is $527.00\n"
                + "Your budget for Test Value for 12/2018 has not been set.  Please set it using budget set.\n"
                + "Your budget for Test Value for 1/2018 has not been set.  Please set it using budget set.\n"
                + "Your budget for Test Value for 2/2018 has not been set.  Please set it using budget set.\n"
                + "Budget for sweets for 12/2018 is $105.00\n"
                + "Budget for sweets for 1/2019 is $105.00\n"
                + "Budget for sweets for 2/2019 is $105.00\n", newUi.returnResponse());
    }

    @Test
    public void testSavingsBudgetCommand() throws MooMooException, IOException  {
        File budgetFile = File.createTempFile("budget", ".txt");
        budgetFile.deleteOnExit();

        File scheduleFile = File.createTempFile("schedule", ".txt");
        scheduleFile.deleteOnExit();

        File expenditureFile = File.createTempFile("expenditure", ".txt");
        expenditureFile.deleteOnExit();

        CategoryListStub newCatList = new CategoryListStub();
        newCatList.add(null);

        ArrayList<String> categories = new ArrayList<>();

        categories.add("slippers");
        categories.add("window");
        categories.add("laptop");
        categories.add("Dogs");

        ArrayList<Double> budgets = new ArrayList<>();

        budgets.add(875.0);
        budgets.add(938.0);
        budgets.add(75.89);
        budgets.add(100.58);

        for (int i = 0; i < newCatList.getCategoryList().size(); ++i) {
            newCatList.getCategoryList().get(i).add(null);
        }

        CategoryStub newCategory = new CategoryStub();
        ScheduleListStub newCalendar = new ScheduleListStub();
        UiStub newUi = new UiStub();
        StorageStub newStorage = new StorageStub();
        Budget newBudget = new Budget();

        LocalDate startDate = LocalDate.of(2017, 1, 15);
        LocalDate endDate = LocalDate.of(2019, 12, 15);

        SetBudgetCommand setBudget = new SetBudgetCommand(false, categories, budgets, startDate, endDate);
        setBudget.execute(newCalendar, newBudget, newCatList, newCategory, newUi, newStorage);

        startDate = LocalDate.of(2019, 9, 15);
        endDate = LocalDate.of(2019, 11, 15);

        SavingsBudgetCommand savingsBudget = new SavingsBudgetCommand(false, categories, startDate, endDate);
        savingsBudget.execute(newCalendar, newBudget, newCatList, newCategory, newUi, newStorage);

        assertEquals("slippers category does not exist. Please create it first.\n"
                + "Your savings for window from SEPTEMBER 2019 to NOVEMBER 2019 is: $2549.00\n"
                + "You have overspent for your budget for laptop from SEPTEMBER 2019 to NOVEMBER 2019 by: $37.33\n"
                + "dogs category does not exist. Please create it first.\n"
                + "Your total savings: $2511.67\n", newUi.returnResponse());

        savingsBudget = new SavingsBudgetCommand(false, categories, startDate, null);
        savingsBudget.execute(newCalendar, newBudget, newCatList, newCategory, newUi, newStorage);

        assertEquals("slippers category does not exist. Please create it first.\n"
                + "Your savings for window for SEPTEMBER 2019 is: $788.00\n"
                + "You have overspent your budget for laptop for SEPTEMBER 2019 by $74.11\n"
                + "dogs category does not exist. Please create it first.\n"
                + "Your total savings: $713.89\n", newUi.returnResponse());

        categories.clear();
        assertEquals(0, categories.size());

        savingsBudget = new SavingsBudgetCommand(false, categories, startDate, null);
        savingsBudget.execute(newCalendar, newBudget, newCatList, newCategory, newUi, newStorage);

        assertEquals("The budget for shoes for 9/2019 does not exist. Please set it using budget set.\n"
                + "The budget for food for 9/2019 does not exist. Please set it using budget set.\n"
                + "Your savings for window for SEPTEMBER 2019 is: $788.00\n"
                + "The budget for places to go for 9/2019 does not exist. Please set it using budget set.\n"
                + "The budget for sweets for 9/2019 does not exist. Please set it using budget set.\n"
                + "You have overspent your budget for laptop for SEPTEMBER 2019 by $74.11\n"
                + "Your total savings: $713.89\n", newUi.returnResponse());

        categories.clear();
        assertEquals(0, categories.size());
        categories.add("laptop");
        assertEquals(1, categories.size());

        startDate = LocalDate.of(2017, 9, 15);
        endDate = LocalDate.of(2019, 2, 15);

        savingsBudget = new SavingsBudgetCommand(false, categories, startDate, endDate);
        savingsBudget.execute(newCalendar, newBudget, newCatList, newCategory, newUi, newStorage);

        assertEquals("Your savings for laptop from SEPTEMBER 2017 to FEBRUARY 2019 is: $1066.02\n"
                + "Your total savings: $1066.02\n", newUi.returnResponse());
    }
}