package moomoo.task;

import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Holds a map for the budget for each category.
 */
public class Budget {
    private HashMap<String, HashMap<String, Double>> categoryBudgets;
    private DecimalFormat df;
    private double totalBudget;

    /**
     * Initializes values to default values if no previous budget information is found.
     * Initializes DecimalFormat to force doubles to display with 2 decimal places.
     */
    public Budget() {
        this.categoryBudgets = new HashMap<String, HashMap<String, Double>>();
        this.totalBudget = 0;
        df = new DecimalFormat("#.00");
    }

    /**
     * Takes in budget set by user and set budget variable.
     * Initializes DecimalFormat to force doubles to display with 2 decimal places.
     */
    public Budget(HashMap<String, HashMap<String, Double>>  newBudget) {
        this.categoryBudgets = newBudget;
        this.totalBudget = 0;
        df = new DecimalFormat("#.00");
    }

    @Override
    public String toString() {
        String returnVal = "";
        Iterator budgetIterator = categoryBudgets.entrySet().iterator();
        while (budgetIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) budgetIterator.next();
            returnVal += "Your budget for " + mapElement.getKey() + " is: $" + df.format(mapElement.getValue()) + "\n";
        }
        return returnVal;
    }

    /**
     * Returns the budget for corresponding category for the current month.
     * @param category Category to get budget for
     * @return Budget of category.
     */
    public double getBudgetFromCategory(String category) {
        LocalDate currentTime = LocalDate.now();
        String key = "01/" + currentTime.getMonthValue() + "/" + currentTime.getYear();
        if (categoryBudgets.containsKey(key)) {
            if (categoryBudgets.get(key).containsKey(category)) {
                return categoryBudgets.get(key).get(category);
            }
        }
        return 0;
    }

    /**
     * Returns the budget for the corresponding category, month and year.
     * @param category Category to return budget for
     * @param month Month to return budget for
     * @param year Year to return budget for
     * @return Budget to return
     */
    public double getBudgetFromCategoryMonthYear(String category, int month, int year) {
        String newDate = month + "/" + year;

        if (this.categoryBudgets.containsKey(newDate)) {
            if (this.categoryBudgets.get(newDate).containsKey(category)) {
                return this.categoryBudgets.get(newDate).get(category);
            }
            return 0;
        } else {
            return 0;
        }
    }

    /**
     * Adds budget to the HashMap and re-adds to total budget if budget is changed or added.
     * @param category Category to which the budget was set
     * @param budget Budget to set for the corresponding category
     */
    public void addNewBudgetMonthYear(String category, double budget, int month, int year) {
        String newDate = month + "/" + year;

        if (this.categoryBudgets.containsKey(category)) {
            totalBudget -= this.categoryBudgets.get(newDate).get(category);
        }
        if (this.categoryBudgets.containsKey(newDate)) {
            this.categoryBudgets.get(newDate).put(category, budget);
        } else {
            HashMap<String, Double> newHashMap = new HashMap<>();
            newHashMap.put(category, budget);
            this.categoryBudgets.put(newDate, newHashMap);
        }
        totalBudget += budget;
    }

    public HashMap<String, HashMap<String, Double>> getBudget() {
        return this.categoryBudgets;
    }

    public int getBudgetSize() {
        return this.categoryBudgets.size();
    }

    public double getTotalBudget() {
        return this.totalBudget;
    }

}