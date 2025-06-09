package com.normalizer.panels;

import com.normalizer.db.DatabaseManager;
import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Panel to display raw book data before normalization
 * @author USER
 */
public class ViewRawData extends JPanel {
    private JTable dataTable;
    private DefaultTableModel tableModel;
    
    public ViewRawData() {
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Initialize table model with column names
        String[] columnNames = {"ID", "Book Name", "Author", "ISBN", "Year", "Category", "Pages"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        dataTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public final void loadData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get all books from database
        List<Map<String, Object>> books = DatabaseManager.getAllBooks();
        
        // Populate table model
        for (Map<String, Object> book : books) {
            Object[] row = {
                book.get("book_id"),
                book.get("book_name"),
                book.get("author"),
                book.get("isbn"),
                book.get("year_published"),
                book.get("category"),
                book.get("pages")
            };
            tableModel.addRow(row);
        }
    }
}