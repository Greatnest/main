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
import java.util.ArrayList;

/**
 * Edits the budgets for respective categories.
 */
public class EditBudgetCommand extends Command {
    private ArrayList<String> categories;
    private ArrayList<Double> budgets;
    private LocalDate start;
    private LocalDate end;
    private DecimalFormat df;

    /**
     * Initializes the Edit budget command.
     * @param isExit Boolean variable to determine if program should exit.
     * @param categories List of categories to edit.
     * @param budgets List of budgets to change corresponding categories to.
     */
    public EditBudgetCommand(boolean isExit, ArrayList<String> categories, ArrayList<Double> budgets,
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
                int numberOfYears = end.getYear() - start.getYear();
                if (categoryBudget <= 0) {
                    outputValue += "Please set your budget for " + categoryName + " to a value more than 0\n";
                    continue;
                }
                if (numberOfYears > 0) {
                    int startMonthValue = start.getMonthValue();
                    int endMonthValue = 12;
                    for (int currentYear = start.getYear(); currentYear <= end.getYear(); ++currentYear) {
                        for (int currentMonth = startMonthValue; currentMonth <= endMonthValue; ++currentMonth) {
                            double currentBudget = budget.getBudgetFromCategoryMonthYear(categoryName,
                                    currentMonth, currentYear);

                            if (currentBudget == categoryBudget) {
                                continue;
                            }

                            if (budget.getBudgetFromCategoryMonthYear(categoryName, currentMonth, currentYear) == 0) {
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
                        double currentBudget = categoryBudget;
                        if (currentBudget == categoryBudget) {
                            continue;
                        }

                        if (budget.getBudgetFromCategoryMonthYear(categoryName, currentMonth, start.getYear()) == 0) {
                            continue;
                        }

                        isUpdated = true;
                        budget.addNewBudgetMonthYear(categoryName, categoryBudget, currentMonth, start.getYear());
                    }
                }
                outputValue += "You have changed the budget for " + categoryName + " to $"
                        + df.format(categoryBudget) + ". Please view the changed budget using budget list.\n";

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
