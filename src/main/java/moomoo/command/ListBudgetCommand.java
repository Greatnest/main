package moomoo.command;

import moomoo.task.Budget;
import moomoo.task.ScheduleList;
import moomoo.task.Storage;
import moomoo.task.Ui;
import moomoo.task.MooMooException;
import moomoo.task.category.Category;
import moomoo.task.category.CategoryList;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Lists the budget for respective categories.
 */
public class ListBudgetCommand extends Command {
    private ArrayList<String> categories;
    private DecimalFormat df;
    private LocalDate start;
    private LocalDate end;

    /**
     * Initializes the command to list the set budgets.
     *
     * @param isExit     Boolean variable to determine if program should exit.
     * @param categories List of categories to list the budgets for.
     */
    public ListBudgetCommand(boolean isExit, ArrayList<String> categories, LocalDate start, LocalDate end) {
        super(isExit, "");
        this.categories = categories;
        this.start = start;
        this.end = end;
        df = new DecimalFormat("#.00");
    }

    @Override
    public void execute(ScheduleList calendar, Budget budget, CategoryList catList, Category category,
                        Ui ui, Storage storage) throws MooMooException {
        String outputValue = "";
        double currentBudget = 0;
        if (categories.size() == 0) {
            for (int i = 0; i < catList.getCategoryList().size(); ++i) {
                categories.add(catList.getCategoryList().get(i).toString());
            }
        }
        for (int i = 0; i < categories.size(); ++i) {
            String categoryName = categories.get(i);
            int numberOfYears = end.getYear() - start.getYear();

            if (numberOfYears > 0) {
                int startMonthValue = start.getMonthValue();
                int endMonthValue = 12;
                for (int currentYear = start.getYear(); currentYear <= end.getYear(); ++currentYear) {
                    for (int currentMonth = startMonthValue; currentMonth <= endMonthValue; ++currentMonth) {
                        if (budget.getBudgetFromCategoryMonthYear(categoryName, currentMonth, currentYear) == 0) {
                            outputValue += "Your budget for " + categoryName + " for " + currentMonth + "/"
                                    + start.getYear() + " has not been set. "
                                    + " Please set it using budget set.\n";
                            continue;
                        }
                        currentBudget = budget.getBudgetFromCategoryMonthYear(categoryName, currentMonth, currentYear);
                        if (currentBudget == 0) {
                            outputValue += "Budget for " + categoryName + " has not been set\n";
                            continue;
                        }
                        outputValue += "Budget for " + categoryName + " for " + currentMonth + "/" + currentYear
                                + " is $" + df.format(currentBudget) + "\n";
                    }
                    startMonthValue = 1;
                    if (currentYear == end.getYear() - 1) {
                        endMonthValue = end.getMonthValue();
                    }
                }
            } else {
                for (int currentMonth = start.getMonthValue(); currentMonth < end.getMonthValue() + 1;
                     ++currentMonth) {
                    if (budget.getBudgetFromCategoryMonthYear(categoryName, currentMonth, start.getYear()) == 0) {
                        outputValue += "Your budget for " + categoryName + " for " + currentMonth + "/"
                                + start.getYear() + " has not been set. "
                                + " Please set it using budget set.\n";
                        continue;
                    }
                    currentBudget = budget.getBudgetFromCategoryMonthYear(categoryName, currentMonth, start.getYear());
                    if (currentBudget == 0) {
                        outputValue += "Budget for " + categoryName + " has not been set\n";
                        continue;
                    }
                    outputValue += "Budget for " + categoryName + " for " + currentMonth + "/"
                            + start.getYear() + " is $"
                            + df.format(currentBudget) + "\n";
                }
            }
        }
        ui.setOutput(outputValue);
        return;
    }
}
