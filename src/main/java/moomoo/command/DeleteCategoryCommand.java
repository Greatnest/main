package moomoo.command;

import moomoo.task.Budget;
import moomoo.task.CategoryList;
import moomoo.task.Storage;
import moomoo.task.MooMooException;
import moomoo.task.Ui;
import moomoo.task.Category;

public class DeleteCategoryCommand extends Command {

    public DeleteCategoryCommand() {
        super(false, "");
    }

    @Override
    public void execute(Budget budget, CategoryList categoryList, Category category, Ui ui, Storage storage)
            throws MooMooException {
        super.execute(budget, categoryList, category, ui, storage);

        categoryList.list(ui);
        ui.showEnterCategoryMessage();
        int categoryNumber = ui.readNumber() - 1;
        ui.showRemovedCategoryMessage(categoryList.get(categoryNumber));
        categoryList.deleteCategory(categoryNumber);
    }
}
