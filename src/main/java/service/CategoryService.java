package service;

import dao.CategoryDAO;
import model.Category;

import java.util.List;

public class CategoryService {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    public String addCategory(String categoryName, String description) {
        if (categoryName.isBlank()) {
            return "Category name is required.";
        }

        if (categoryDAO.categoryExists(categoryName)) {
            return "Category already exists.";
        }

        Category category = new Category(categoryName, description);

        return categoryDAO.addCategory(category) ? "SUCCESS" : "Failed to add category.";
    }

    public String updateCategory(int categoryId, String categoryName, String description) {
        if (categoryId <= 0) {
            return "Select a category first.";
        }

        if (categoryName.isBlank()) {
            return "Category name is required.";
        }

        Category category = new Category(categoryId, categoryName, description);

        return categoryDAO.updateCategory(category) ? "SUCCESS" : "Failed to update category.";
    }

    public boolean deleteCategory(int categoryId) {
        if (categoryId <= 0) {
            return false;
        }

        return categoryDAO.deleteCategory(categoryId);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }
}