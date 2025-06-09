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
import javax.swing.table.DefaultTableModel;

/**
 * Panel to display the Third Normal Form (3NF) of book data
 * @author USER
 */
public class View3NFPanel extends JPanel {
    private JTable booksTable;
    private JTable authorsTable;
    private JTable categoriesTable;
    private JTable publishYearsTable;
    private JTable idsTable;  // New table for displaying IDs
    private DefaultTableModel booksModel;
    private DefaultTableModel authorsModel;
    private DefaultTableModel categoriesModel;
    private DefaultTableModel yearsModel;
    private DefaultTableModel idsModel;  // DefaultTableModel for IDs table

    public View3NFPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout());

        // Add explanation label
        JLabel explainLabel = new JLabel("<html><p>Third Normal Form (3NF): No transitive dependencies, and all attributes depend directly on the primary key.</p></html>");
        contentPanel.add(explainLabel, BorderLayout.NORTH);

        // Panel to hold the tables
        JPanel tablesPanel = new JPanel(new GridLayout(3, 2, 10, 10));  // Increase rows for the new IDs table

        // Books table
        String[] booksColumns = {"Book ID", "Book Name"};
        booksModel = new DefaultTableModel(booksColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        booksTable = new JTable(booksModel);
        JScrollPane booksScroll = new JScrollPane(booksTable);
        booksScroll.setBorder(javax.swing.BorderFactory.createTitledBorder("Books"));
        tablesPanel.add(booksScroll);

        // Authors table
        String[] authorsColumns = {"Author ID", "Author Name"};
        authorsModel = new DefaultTableModel(authorsColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        authorsTable = new JTable(authorsModel);
        JScrollPane authorsScroll = new JScrollPane(authorsTable);
        authorsScroll.setBorder(javax.swing.BorderFactory.createTitledBorder("Authors"));
        tablesPanel.add(authorsScroll);

        // Categories table
        String[] categoriesColumns = {"Category ID", "Category Name"};
        categoriesModel = new DefaultTableModel(categoriesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        categoriesTable = new JTable(categoriesModel);
        JScrollPane categoriesScroll = new JScrollPane(categoriesTable);
        categoriesScroll.setBorder(javax.swing.BorderFactory.createTitledBorder("Categories"));
        tablesPanel.add(categoriesScroll);

        // Years table
        String[] yearsColumns = {"Year ID", "Year Published"};
        yearsModel = new DefaultTableModel(yearsColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        publishYearsTable = new JTable(yearsModel);
        JScrollPane yearsScroll = new JScrollPane(publishYearsTable);
        yearsScroll.setBorder(javax.swing.BorderFactory.createTitledBorder("Years"));
        tablesPanel.add(yearsScroll);

        // New IDs table (Book ID, Author ID, Category ID, Year ID)
        String[] idsColumns = {"Book ID", "Author ID", "Category ID", "Year ID"};
        idsModel = new DefaultTableModel(idsColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        idsTable = new JTable(idsModel);
        JScrollPane idsScroll = new JScrollPane(idsTable);
        idsScroll.setBorder(javax.swing.BorderFactory.createTitledBorder("IDs Relationships"));
        tablesPanel.add(idsScroll);

        contentPanel.add(tablesPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

    // Helper method to split and flatten comma-separated values into atomic entries
    private String[] flattenCommaValues(String value) {
        if (value == null || value.trim().isEmpty()) {
            return new String[]{}; 
        }
        return value.split("\\s*,\\s*"); // Split by commas and trim whitespace
    }

    public final void loadData() {
        // Clear existing data
        booksModel.setRowCount(0);
        authorsModel.setRowCount(0);
        categoriesModel.setRowCount(0);
        yearsModel.setRowCount(0);
        idsModel.setRowCount(0);  // Clear the IDs table

        // Get all books from database
        List<Map<String, Object>> books = DatabaseManager.getAllBooks();

        // Normalize authors, categories, and years into separate tables
        Map<String, Integer> authors = new HashMap<>();
        Map<String, Integer> categories = new HashMap<>();
        Map<Integer, Integer> years = new HashMap<>();
        int authorId = 1;
        int categoryId = 1;
        int yearId = 1;

        // First pass: extract unique authors, categories, and years
        for (Map<String, Object> book : books) {
            String[] authorsArray = flattenCommaValues((String) book.get("author"));
            String[] categoriesArray = flattenCommaValues((String) book.get("category"));
            int year = (int) book.get("year_published");

            // Normalize authors
            for (String author : authorsArray) {
                if (!authors.containsKey(author)) {
                    authors.put(author, authorId++);
                    Object[] authorRow = {authors.get(author), author};
                    authorsModel.addRow(authorRow);
                }
            }

            // Normalize categories
            for (String category : categoriesArray) {
                if (!categories.containsKey(category)) {
                    categories.put(category, categoryId++);
                    Object[] categoryRow = {categories.get(category), category};
                    categoriesModel.addRow(categoryRow);
                }
            }

            // Normalize years
            if (!years.containsKey(year)) {
                years.put(year, yearId++);
                Object[] yearRow = {years.get(year), year};
                yearsModel.addRow(yearRow);
            }
        }

        // Second pass: populate books table with foreign keys
        for (Map<String, Object> book : books) {
            String[] authorsArray = flattenCommaValues((String) book.get("author"));
            String[] categoriesArray = flattenCommaValues((String) book.get("category"));
            int year = (int) book.get("year_published");

            // For each author-category-year combination, create a row in the books table
            for (String author : authorsArray) {
                for (String category : categoriesArray) {
                    Object[] bookRow = {
                        book.get("book_id"),
                        book.get("book_name"),
                        authors.get(author),      // Foreign key to authors
                        book.get("isbn"),
                        years.get(year),          // Foreign key to years
                        categories.get(category), // Foreign key to categories
                        book.get("pages")
                    };
                    booksModel.addRow(bookRow);

                    // Add the relationship to the IDs table
                    Object[] idsRow = {
                        book.get("book_id"),
                        authors.get(author),      // Foreign key to authors
                        categories.get(category), // Foreign key to categories
                        years.get(year)           // Foreign key to years
                    };
                    idsModel.addRow(idsRow);  // Add relationship to IDs table
                }
            }
        }
    }
}
