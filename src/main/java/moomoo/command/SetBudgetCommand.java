package moomoo.command;

import moomoo.task.Budget;
import moomoo.task.category.Category;
import moomoo.task.category.CategoryList;
import moomoo.task.MooMooException;
import moomoo.task.ScheduleList;
import moomoo.task.Storage;
import moomoo.task.Ui;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Sets the budget for respective categories.
 */
public class SetBudgetCommand extends Command {
    private ArrayList<String> categories;
    private ArrayList<Double> budgets;
    private LocalDate start;
    private LocalDate end;
    private DecimalFormat df;

    /**
     * Initializes the set budget command.
     * @param isExit Boolean variable to determine if program should exit.
     * @param categories List of categories to set the budget for.
     * @param budgets List of budgets to set the corresponding categories to.
     */
    public SetBudgetCommand(boolean isExit, ArrayList<String> categories, ArrayList<Double> budgets,
                            LocalDate start, LocalDate end) {
        super(isExit, "");
        this.categories = categories;
        this.budgets = budgets;
        this.start = start;
        this.end = end;
        df = new DecimalFormat("#.00");
    }

    @Override
    public void execute(ScheduleList calendar, Budget budget, CategoryList catList, Category category,
                        Ui ui, Storage storage) throws MooMooException {
        String outputValue = "";
        boolean isUpdated = false;

        for (int i = 0; i < categories.size(); ++i) {
            String categoryName = categories.get(i).toLowerCase();
            double categoryBudget = budgets.get(i);
            if (catList.get(categoryName) != null) {
                if (categoryBudget <= 0) {
                    outputValue += "Please set your budget for " + categoryName + " to a value more than 0\n";
                    continue;
                }
                int numberOfYears = end.getYear() - start.getYear();

                if (numberOfYears > 0) {
                    int startMonthValue = start.getMonthValue();
                    int endMonthValue = 12;
                    for (int currentYear = start.getYear(); currentYear <= end.getYear(); ++currentYear) {
                        for (int currentMonth = startMonthValue; currentMonth <= endMonthValue; ++currentMonth) {
                            if (budget.getBudgetFromCategoryMonthYear(categoryName, currentMonth, currentYear) != 0) {
                                continue;
                            }
                            isUpdated = true;
                            budget.addNewBudgetMonthYear(categoryName, categoryBudget, currentMonth, currentYear);
                        }
                        startMonthValue = 1;
                        if (currentYear == end.getYear() - 1) {
                            endMonthValue = end.getMonthValue();
                        }
                    }

                } else {
                    for (int currentMonth = start.getMonthValue(); currentMonth < end.getMonthValue() + 1;
                         ++currentMonth) {
                        if (budget.getBudgetFromCategoryMonthYear(categoryName, currentMonth, start.getYear()) != 0) {
                            continue;
                        }
                        isUpdated = true;
                        budget.addNewBudgetMonthYear(categoryName, categoryBudget, currentMonth, start.getYear());
                    }
                }
                outputValue += "You have set $" + df.format(categoryBudget) + " as the budget for "
                        + categoryName + ". If the budget has already been set, no changes will be done. "
                        + "Please use budget edit.\n";
            } else {
                outputValue += categoryName + " category does not exist. Please add it first.\n";
            }
        }

        ui.setOutput(outputValue);
        if (isUpdated) {
            storage.saveBudgetToFile(budget);
        }
    }

}
