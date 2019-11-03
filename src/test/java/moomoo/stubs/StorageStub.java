package moomoo.stubs;

import moomoo.task.Budget;
import moomoo.task.category.Category;
import moomoo.task.Storage;
import moomoo.task.Ui;

import java.util.ArrayList;
import java.util.HashMap;

public class StorageStub extends Storage {
    @Override
    public void saveBudgetToFile(Budget budget) {
    }

    @Override
    public HashMap<String, HashMap<String, Double>> loadBudget(ArrayList<Category> catList, Ui ui) {
        HashMap<String, HashMap<String, Double>> newHashMap = new HashMap<>();
        return newHashMap;
    }
}
