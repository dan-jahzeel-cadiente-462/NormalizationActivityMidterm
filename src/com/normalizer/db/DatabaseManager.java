package com.normalizer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages database connections and operations
 * @author Dan Jahzeel Cadiente
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/normalization_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.err.println("Database connection successful, Username: " + DB_USER);
            initializeDatabase();
        } catch (ClassNotFoundException | SQLException err) {
            System.err.println("Database initialization failed: " + err.getMessage());
        }
    }
    
    private static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create books table if it doesn't exist
            stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                "book_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "book_name VARCHAR(255) NOT NULL, " +
                "year_published INT CHECK(year_published BETWEEN 1000 and 9999), " +
                "isbn VARCHAR(20) UNIQUE NOT NULL, " +
                "author VARCHAR(255) NOT NULL, " +
                "category VARCHAR(255) NOT NULL, " +
                "pages INT CHECK(pages > 0 AND pages <= 10000)" +
                ")");
            
            System.err.println("Tables initialized successfully");
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
    
    /**
     * Add a new book to the database
     */
    public static boolean addBook(String book, String author, String isbn, int year, String category, int pages) {
        String sql = "INSERT INTO books (book_name, author, isbn, year_published, category, pages) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, book);
            pstmt.setString(2, author);
            pstmt.setString(3, isbn);
            pstmt.setInt(4, year);
            pstmt.setString(5, category);
            pstmt.setInt(6, pages);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get all books from the database (raw data)
     */
    public static List<Map<String, Object>> getAllBooks() {
        List<Map<String, Object>> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> book = new HashMap<>();
                book.put("book_id", rs.getInt("book_id"));
                book.put("book_name", rs.getString("book_name"));
                book.put("author", rs.getString("author"));
                book.put("isbn", rs.getString("isbn"));
                book.put("year_published", rs.getInt("year_published"));
                book.put("category", rs.getString("category"));
                book.put("pages", rs.getInt("pages"));
                books.add(book);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving books: " + e.getMessage());
        }
        
        return books;
    }
    
    /**
     * Check if ISBN already exists
     */
    public static boolean isbnExists(String isbn) {
        String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking ISBN: " + e.getMessage());
        }
        
        return false;
    }
}