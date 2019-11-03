package moomoo.task;

import moomoo.task.category.Category;
import moomoo.task.category.Expenditure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Handles storage and retrieval of the tasks.
 */
public class Storage {
    private DecimalFormat df;
    private String budgetFilePath;
    private String scheduleFilePath;
    private String categoryFilePath;
    private String expenditureFilePath;

    /**
     * Initializes empty Storage object.
     */
    public Storage() {

    }

    /**
     * Initializes storage and the filepath for each file.
     * @param budgetFilePath File path to store the budget into.
     * @param scheduleFilePath File path to store all categories
     */
    public Storage(String budgetFilePath, String scheduleFilePath, String categoryFilePath,
                   String expenditureFilePath) {
        this.budgetFilePath = budgetFilePath;
        this.scheduleFilePath = scheduleFilePath;
        this.categoryFilePath = categoryFilePath;
        this.expenditureFilePath = expenditureFilePath;
        df = new DecimalFormat("#.00");
    }

    /**
     * Loads in categories from an existing file into a created ArrayList object.
     * @return ArrayList object consisting of the categories read from the file.
     * @throws MooMooException Thrown when the file does not exist
     */
    public ArrayList<Category> loadCategories() throws MooMooException {
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        try {
            File myNewFile = new File(categoryFilePath);
            if (myNewFile.createNewFile()) {
                return populateDefaultCategories(categoryArrayList);
            } else {
                List<String> input = Files.readAllLines(Paths.get(this.categoryFilePath));
                Category newCategory = new Category("misc");
                for (String s : input) {
                    if (s.startsWith("c/")) {
                        newCategory = new Category(s.replace("c/", ""));
                        categoryArrayList.add(newCategory);
                    } else if (categoryArrayList.isEmpty()) {
                        categoryArrayList.add(newCategory);
                        saveCategoryToFile("misc");
                    }
                }
                return categoryArrayList;
            }
        } catch (IOException e) {
            throw new MooMooException("Unable to read file. Please retry again.");
        }
    }

    /**
     * Creates a populated array of default categories.
     * @param categoryArrayList category list
     * @return populated category list
     * @throws MooMooException throws exception if file cannot be found
     */
    private ArrayList<Category> populateDefaultCategories(ArrayList<Category> categoryArrayList)
            throws MooMooException {
        categoryArrayList.add(new Category("misc"));
        categoryArrayList.add(new Category("food"));
        categoryArrayList.add(new Category("transportation"));
        categoryArrayList.add(new Category("bills"));
        saveCategoryToFile("misc");
        saveCategoryToFile("food");
        saveCategoryToFile("transportation");
        saveCategoryToFile("bills");
        return categoryArrayList;
    }

    /**
     * Loads in budgetFile not found. New file will be created from an existing file into a created HashMap object.
     * @return HashMap object consisting of the categories and corresponding budget read from file.
     */
    public HashMap<String, HashMap<String, Double>> loadBudget(ArrayList<Category> catList, Ui ui) {
        try {
            if (Files.isRegularFile(Paths.get(this.budgetFilePath))) {
                HashMap<String, HashMap<String, Double>> loadedBudgets = new HashMap<>();
                List<String> readInput = Files.readAllLines(Paths.get(this.budgetFilePath));
                LocalDate date;
                String stringDate = "";

                for (int i = 0; i < readInput.size(); ++i) {
                    String input = readInput.get(i);
                    if (input.contains("|")) {
                        if (stringDate == "") {
                            throw new MooMooException("Your data file has been corrupted. Please delete it.");
                        }
                        String[] splitInput = input.split("\\|");
                        if (loadedBudgets.containsKey(stringDate)) {
                            try {
                                loadedBudgets.get(stringDate).put(splitInput[0], Double.parseDouble(splitInput[1]));
                            } catch (NumberFormatException e) {
                                throw new MooMooException("Your data file has been corrupted. Please delete it.");
                            }
                        } else {
                            HashMap<String, Double> newHashMap = new HashMap<>();
                            try {
                                newHashMap.put(splitInput[0], Double.parseDouble(splitInput[1]));
                            } catch (NumberFormatException e) {
                                throw new MooMooException("Your data file has been corrupted. Please delete it.");
                            }
                            loadedBudgets.put(stringDate, newHashMap);
                        }
                    } else {
                        stringDate = input;
                        date = parseMonth(input);
                        if (date == null) {
                            throw new MooMooException("Your data file has been corrupted. Please delete it.");
                        }
                    }
                }
                return loadedBudgets;
            } else {
                ui.setOutput("Budget File not found. New file will be created");
                createFileAndDirectory(this.budgetFilePath);
                return null;
            }
        } catch (IOException | MooMooException e) {
            ui.setOutput("Unable to write to file. Please retry again.");
        }
        return null;
    }

    /**
     * Loads scheduled payments from file into an ArrayList object.
     * @return ArrayList object consisting of the scheduled payments read from the file
     */
    public ArrayList<SchedulePayment> loadCalendar(Ui ui) {
        ArrayList<SchedulePayment> scheduleArray = new ArrayList<>();
        try {
            if (Files.isRegularFile(Paths.get(this.scheduleFilePath))) {
                List<String> input = Files.readAllLines(Paths.get(this.scheduleFilePath));
                for (String s : input) {
                    if (s.startsWith("d/")) {
                        String[] splitInput = s.split(" ", 2);
                        String date = splitInput[0].replace("d/","");
                        String task = splitInput[1].replace("n/", "");
                        SchedulePayment day = new SchedulePayment(date, task);
                        scheduleArray.add(day);
                    }
                }
                return scheduleArray;
            } else {
                ui.setOutput("Schedule File not found. New file will be created");
            }
        } catch (IOException e) {
            ui.setOutput("Unable to read file. Please retry again.");
        }
        return null;
    }

    /**
     * Creates the directory and file as given by the file path initialized in the constructor.
     */
    private void createFileAndDirectory(String filePath) throws MooMooException {
        try {
            File myNewFile = new File(filePath);
            myNewFile.getParentFile().mkdir();
            myNewFile.createNewFile();
        } catch (IOException e) {
            throw new MooMooException("Unable to create file. Your data will not be saved.");
        }
    }

    /**
     * Creates a file if necessary and stores each category and its budget into the file.
     * @param category Budget object that stores the budget for each category
     * @throws MooMooException thrown if file cannot be written to.
     */
    public void deleteCategoryFromFile(String category) throws MooMooException {
        try {
            List<String> data = Files.readAllLines(Paths.get(this.categoryFilePath));
            for (String iterator : data) {
                if (iterator.contentEquals("c/" + category)) {
                    data.remove(iterator);
                    break;
                }
            }
            Files.write(Paths.get(this.categoryFilePath), data);
        } catch (IOException e) {
            throw new MooMooException("Unable to write to file. Please retry again.");
        }

    }

    /**
     * Creates a file if necessary and stores each category and its budget into the file.
     * @param category Budget object that stores the budget for each category
     * @throws MooMooException thrown if file cannot be written to.
     */
    public void saveCategoryToFile(String category) throws MooMooException {
        createFileAndDirectory(this.categoryFilePath);
        try {
            String newCategory = Files.readString(Paths.get(this.categoryFilePath));
            newCategory = ("c/" + category + "\n" + newCategory);
            Files.writeString(Paths.get(this.categoryFilePath), newCategory);
        } catch (IOException e) {
            throw new MooMooException("Unable to write to file. Please retry again.");
        }
    }

    /**
     * Creates a file if necessary and stores each expenditure and its category into the file.
     * @param expenditure Expenditure object that stores the name, amount and date for each expenditure.
     * @param category Category that the expenditure is stored in.
     * @throws MooMooException thrown if file cannot be written to.
     */
    public void saveExpenditureToFile(Expenditure expenditure, String category) throws MooMooException {
        createFileAndDirectory(this.expenditureFilePath);
        try {
            String newExpenditure = Files.readString(Paths.get(this.expenditureFilePath));
            newExpenditure += (category + " | " + expenditure.toString() + " | " + expenditure.getCost()
                + " | " + expenditure.getDate() + "\n");
            Files.writeString(Paths.get(this.expenditureFilePath), newExpenditure);
        } catch (IOException e) {
            throw new MooMooException("Unable to write to file. Please retry again.");
        }
    }

    /**
     * Creates a file if necessary and stores each category and its budget into the file.
     * @param budget Budget object that stores the budget for each category
     * @throws MooMooException thrown if file cannot be written to.
     */
    public void saveBudgetToFile(Budget budget) throws MooMooException {
        createFileAndDirectory(this.budgetFilePath);
        String toSave = "";
        Iterator<Map.Entry<String, HashMap<String, Double>>> monthIterator = budget.getBudget().entrySet().iterator();

        while (monthIterator.hasNext()) {
            Map.Entry<String, HashMap<String, Double>> monthElement = monthIterator.next();
            HashMap<String, Double> budgetHashMap;
            budgetHashMap = monthElement.getValue();
            Iterator<Map.Entry<String, Double>> budgetIterator = budgetHashMap.entrySet().iterator();
            toSave += "01/" + monthElement.getKey() + "\n";
            while (budgetIterator.hasNext()) {
                Map.Entry<String, Double> budgetElement = budgetIterator.next();
                toSave += budgetElement.getKey() + "|" + budgetElement.getValue() + '\n';
            }

        }
        try {
            Files.writeString(Paths.get(this.budgetFilePath), toSave);
        } catch (Exception e) {
            throw new MooMooException("Unable to write to budget file. Please retry again.");
        }
    }

    /**
     * Writes scheduled payments to file.
     */
    public void saveScheduleToFile(ScheduleList calendar) throws MooMooException {
        createFileAndDirectory(this.scheduleFilePath);

        String list = "Schedule: \n";
        for (SchedulePayment c : calendar.fullSchedule) {
            list += "d/" + c.date + " n/" + c.tasks + "\n";
        }
        try {
            Files.writeString(Paths.get(this.scheduleFilePath), list);
        } catch (Exception e) {
            throw new MooMooException("Unable to write to file. Please try again.");
        }
    }

    /**
     * Checks if a category is found in the list of categories.
     * @return true if it exists.
     */
    private boolean isInCategoryList(ArrayList<Category> catList, String value) {
        for (Category cat : catList) {
            if (cat.toString().equals(value)) {
                return true;
            }
        }
        return false;
    }

    private static LocalDate parseMonth(String inputDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");
            return LocalDate.parse(inputDate, formatter);
        } catch (Exception e) {
            return null;
        }
    }
}