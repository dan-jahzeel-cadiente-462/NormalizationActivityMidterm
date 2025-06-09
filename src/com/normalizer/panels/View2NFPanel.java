package com.normalizer.panels;

import com.normalizer.db.DatabaseManager;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Panel to display the Second Normal Form (2NF) of book data with separated authors and categories.
 * Includes relational tables for book-authors and book-categories normalization.
 * @author USER
 */
public class View2NFPanel extends JPanel {
    private JTable booksTable, authorsTable, categoriesTable, bookAuthorsTable, bookCategoriesTable;
    private DefaultTableModel booksModel, authorsModel, categoriesModel, bookAuthorsModel, bookCategoriesModel;

    public View2NFPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout());

        // Explanation Label
        JLabel explainLabel = new JLabel("<html><p><b>Second Normal Form (2NF):</b> Authors and categories are separated into their own tables, ensuring atomicity.</p></html>");
        contentPanel.add(explainLabel, BorderLayout.NORTH);

        // Panel to hold tables with improved layout
        JPanel tablesPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        // Books Table
        booksModel = new DefaultTableModel(new String[]{"Book ID", "Book Name", "ISBN", "Year", "Pages"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        booksTable = new JTable(booksModel);
        JScrollPane booksScroll = new JScrollPane(booksTable);
        booksScroll.setBorder(new TitledBorder("Books"));
        tablesPanel.add(booksScroll);

        // Authors Table
        authorsModel = new DefaultTableModel(new String[]{"Author ID", "Author Name"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        authorsTable = new JTable(authorsModel);
        JScrollPane authorsScroll = new JScrollPane(authorsTable);
        authorsScroll.setBorder(new TitledBorder("Authors"));
        tablesPanel.add(authorsScroll);

        // Categories Table
        categoriesModel = new DefaultTableModel(new String[]{"Category ID", "Category Name"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        categoriesTable = new JTable(categoriesModel);
        JScrollPane categoriesScroll = new JScrollPane(categoriesTable);
        categoriesScroll.setBorder(new TitledBorder("Categories"));
        tablesPanel.add(categoriesScroll);

        // Book-Authors Relationship Table
        bookAuthorsModel = new DefaultTableModel(new String[]{"Book ID", "Author ID"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        bookAuthorsTable = new JTable(bookAuthorsModel);
        JScrollPane bookAuthorsScroll = new JScrollPane(bookAuthorsTable);
        bookAuthorsScroll.setBorder(new TitledBorder("Book-Authors"));
        tablesPanel.add(bookAuthorsScroll);

        // Book-Categories Relationship Table
        bookCategoriesModel = new DefaultTableModel(new String[]{"Book ID", "Category ID"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        bookCategoriesTable = new JTable(bookCategoriesModel);
        JScrollPane bookCategoriesScroll = new JScrollPane(bookCategoriesTable);
        bookCategoriesScroll.setBorder(new TitledBorder("Book-Categories"));
        tablesPanel.add(bookCategoriesScroll);

        contentPanel.add(tablesPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

    // Helper method to split and flatten comma-separated values
    private String[] flattenCommaValues(String value) {
        if (value == null || value.trim().isEmpty()) {
            return new String[]{};
        }
        return value.trim().replaceAll(",+", ",").split("\\s*,\\s*"); // Removes duplicate commas and trims spaces
    }

    public final void loadData() {
        // Clear existing data
        booksModel.setRowCount(0);
        authorsModel.setRowCount(0);
        categoriesModel.setRowCount(0);
        bookAuthorsModel.setRowCount(0);
        bookCategoriesModel.setRowCount(0);

        // Get all books from database
        List<Map<String, Object>> books = DatabaseManager.getAllBooks();

        // Normalization Maps
        Map<String, Integer> authors = new HashMap<>();
        Map<String, Integer> categories = new HashMap<>();
        int authorId = 1, categoryId = 1;

        // First pass: extract unique authors and categories
        for (Map<String, Object> book : books) {
            String[] authorsArray = flattenCommaValues((String) book.get("author"));
            String[] categoriesArray = flattenCommaValues((String) book.get("category"));

            for (String author : authorsArray) {
                if (!authors.containsKey(author)) {
                    authors.put(author, authorId++);
                    authorsModel.addRow(new Object[]{authors.get(author), author});
                }
            }

            for (String category : categoriesArray) {
                if (!categories.containsKey(category)) {
                    categories.put(category, categoryId++);
                    categoriesModel.addRow(new Object[]{categories.get(category), category});
                }
            }
        }

        // Second pass: populate relational tables
        for (Map<String, Object> book : books) {
            int bookId = (int) book.get("book_id");
            String bookName = (String) book.get("book_name");
            String isbn = (String) book.get("isbn");
            int year = (int) book.get("year_published");
            int pages = (int) book.get("pages");

            // Add book record
            booksModel.addRow(new Object[]{bookId, bookName, isbn, year, pages});

            // Assign book-authors relationship
            for (String author : flattenCommaValues((String) book.get("author"))) {
                bookAuthorsModel.addRow(new Object[]{bookId, authors.get(author)});
            }

            // Assign book-categories relationship
            for (String category : flattenCommaValues((String) book.get("category"))) {
                bookCategoriesModel.addRow(new Object[]{bookId, categories.get(category)});
            }
        }
    }
}
