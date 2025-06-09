package com.normalizer.panels;

import com.normalizer.db.DatabaseManager;
import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Panel to display the First Normal Form (1NF) of book data
 * @author USER
 */
public class View1NFPanel extends JPanel {
    private JTable booksTable;
    private DefaultTableModel booksModel;

    public View1NFPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout());

        // Add explanation label
        JLabel explainLabel = new JLabel("<html><p>First Normal Form (1NF): Each attribute contains only atomic values and there is a primary key</p></html>");
        contentPanel.add(explainLabel, BorderLayout.NORTH);

        // Initialize table model with column names
        String[] booksColumns = {"Book ID", "Book Name", "Author", "ISBN", "Year", "Category", "Pages"};
        booksModel = new DefaultTableModel(booksColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        booksTable = new JTable(booksModel);
        JScrollPane scrollPane = new JScrollPane(booksTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    // Helper method to split and format comma-separated values into atomic entries
    private String[] flattenCommaValues(String value) {
        if (value == null || value.trim().isEmpty()) {
            return new String[] {};
        }
        return value.split("\\s*,\\s*"); // Split by commas and trim whitespace
    }

    public final void loadData() {
        // Clear existing data
        booksModel.setRowCount(0);

        // Get all books from the database
        List<Map<String, Object>> books = DatabaseManager.getAllBooks();

        for (Map<String, Object> book : books) {
            // Flatten and iterate over authors and categories
            String[] authors = flattenCommaValues((String) book.get("author"));
            String[] categories = flattenCommaValues((String) book.get("category"));
            String bookName = (String) book.get("book_name");

            for (String author : authors) {
                for (String category : categories) {
                    Object[] row = {
                        book.get("book_id"),  // Primary key
                        bookName,
                        author,
                        book.get("isbn"),
                        book.get("year_published"),
                        category,
                        book.get("pages")
                    };
                    booksModel.addRow(row);
                }
            }
        }
    }
}