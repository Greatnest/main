package moomoo.task;

import moomoo.task.category.Category;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Represents the User Interface to be shown to the user.
 */
public class Ui {
    private String output = null;
    private Scanner inputScanner;

    /**
     * Returns the value to be printed to the GUI.
     * @return String to be printed on the GUI
     */
    public String returnResponse() {
        return this.output;
    }

    /**
     * Prints the welcome message to the User.
     */
    public void showWelcome() {
        print("   \n"
                + "   ^____^________\n"
                + "   ( oo )\\ *  *  )\\/\\\n"
                + "   (____)||----w |  o \n"
                + "         ||     ||   00\n"
                + "   wmwwmWMWMwmWMmwMWWMWMwm\n"
                + "MOOOOOOOO!\n"

                + "Welcome to MooMooMoney! Your one-stop budgeting and expenses tracker!\n"
                + "What can MooMoo do for you today?");
    }

    /**
     * Used to read input from the user.
     * @return String representing the input given by the User
     */
    public String readCommand() {
        this.inputScanner = new Scanner(System.in);
        return this.inputScanner.nextLine();
    }

    /**
     * Used to read input from the user.
     * @return Integer representing the input given by the User
     */
    public int readNumber() {
        this.inputScanner = new Scanner(System.in);
        return this.inputScanner.nextInt();
    }

    /**
     * Sets good bye message to be shown to the User.
     */
    public void showGoodbye() {
        this.output = "Hope you had a great time using MooMooMoney!\n"
                + "See you next time :)";
    }

    /**
     * Returns message of MooMooException that occurs.
     * @param e MooMooException that occurs
     * @return Message of the MooMooException
     */
    public String printException(MooMooException e) {
        this.output = e.getMessage();
        return this.output;
    }

    /**
     * Prints out response from command.
     */
    public void showResponse() {
        try {
            PrintStream out = new PrintStream(System.out, true, "UTF-8");
            out.println(this.output);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.output = "";
    }

    /**
     * Show today's date.
     */
    public String showDate() {
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
        String shortDate = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
        print("\n" + formattedDate + "\n");
        return shortDate;
    }


    /**
     * Prints the error message for the user.
     * @param message error message
     */
    public void showErrorMessage(String message) {
        print(message);
    }


    /**
     * Prompts the user for confirmation.
     * @return value given by user
     */
    public String confirmPrompt(String value) {
        System.out.println(value);
        inputScanner = new Scanner(System.in);

        return inputScanner.nextLine();
    }

    /**
     * Sets the output to be printed.
     * @param output Input value to be printed.
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * Prints out a message enclosed between two lines.
     * @param text message to be printed
     */
    private void print(String text) {
        System.out.println(text);
    }

    /**
     * Prints out when a new category is created.
     * @param categoryName name of the new category
     */
    public void showNewCategoryMessage(String categoryName) {
        print("Ok, I've added a new category named " + categoryName + ".");
    }

    /**
     * Prompts the user to enter a category name.
     */
    public void showAddCategoryMessage() {
        print("Please enter a name for your new category.");
    }

    /**
     * Prints the list of categories.
     * @param categories list of current categories
     */
    public void showCategoryList(String categories) {
        print("These are your current categories:"
                + "\n_______________________________________________"
                + categories
                + "\n_______________________________________________");
    }

    /**
     * Promts the user to enter a category index.
     */
    public void showEnterCategoryMessage() {
        print("Please enter the index of a category.");
    }

    /**
     * Prints out when a category is deleted.
     * @param categoryName name of the new category
     */
    public void showRemovedCategoryMessage(String categoryName) {
        print("Ok, I've deleted the category named " + categoryName + ".");
    }

    /**
     * Prints out when a expenditure is deleted.
     * @param category name of the expenditure to be deleted
     */
    public void showRemovedExpenditureMessage(Category category) {
        print("Ok, I've deleted the expenditure under " + category.toString() + ".");
    }

    /**
     * Prints out when a new expenditure is created.
     * @param categoryName name of the new expenditure
     */
    public void showNewExpenditureMessage(String expenditureName, String categoryName) {
        print("Ok, I've added a new expenditure " + expenditureName + " under " + categoryName + ".");
    }

    /**
     * Promts the user to enter the number corresponding to a month.
     */
    public void showEnterMonthMessage() {
        print("Please enter a month in the format MM.");
    }

    /**
     * Prompts the user to enter a expenditure name.
     */
    public void showAddExpenditureMessage() {
        print("Please enter a name for which category's expenditure and amount with a '-' in between");
    }

    /**
     * Prompts the user to enter a expenditure name.
     */
    public void showDeleteExpenditureMessage() {
        print("Please enter a category's index number and the amount to delete with a '-' in between");
    }

    /**
     * Shows the user his total spending for the month in a category.
     * @param monthlyTotal total spending
     * @param category category user wants to check
     * @param month month that should be totaled
     */
    public void showMonthlyTotal(double monthlyTotal, Category category, int month) {
        print("Your total spending in the month of " + month + " for " + category.toString()
            + " is $" + monthlyTotal + ".");
    }

    /**
     * Prompts the user to enter what to add.
     */
    public void showInputPrompt(String text) {
        print(text);
    }

    /**
     * Shows the user what are the possible commands.
     */
    public void showHelp() {
        String text = "Try one of these commands:\n"
                + "add c/[Category name]\n"
                + "delete c/[Category name]\n"
                + "add n/[Expenditure name] a/[Amount spent] c/[Category] d/[YYYY-MM-DD]\n"
                + "sort\n"
                + "budget set c/[Category Name] b/[Budget] s/[01/2019] e/[10/2019]\n"
                + "budget edit c/[Category Name] b/[Budget] s/[01/2019] e/[10/2019]\n"
                + "budget list c/[Category Name] s/[01/2019] e/[10/2019]\n"
                + "budget savings c/[Category Name] s/[01/2019]\n"
                + "schedule\n"
                + "graph\n"
                + "total";
        print(text);
    }

    public void printMainDisplay(String newMainDisplay) {
        print(newMainDisplay);
    }
}
