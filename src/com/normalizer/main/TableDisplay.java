package com.normalizer.main;

import com.normalizer.db.DatabaseManager;
import com.normalizer.panels.View1NFPanel;
import com.normalizer.panels.View2NFPanel;
import com.normalizer.panels.View3NFPanel;
import com.normalizer.panels.ViewRawData;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 * Displays normalized tables in tabbed panes
 * @author USER
 */
public class TableDisplay extends JFrame {
    private JTabbedPane tabbedPane;
    private ViewRawData rawDataPanel;
    private View1NFPanel view1NFPanel;
    private View2NFPanel view2NFPanel;
    private View3NFPanel view3NFPanel;
    
    public TableDisplay() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Normalized Tables");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        
        tabbedPane = new JTabbedPane();
        
        // Initialize panels
        rawDataPanel = new ViewRawData();
        view1NFPanel = new View1NFPanel();
        view2NFPanel = new View2NFPanel();
        view3NFPanel = new View3NFPanel();
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Raw Data", rawDataPanel);
        tabbedPane.addTab("1NF", view1NFPanel);
        tabbedPane.addTab("2NF", view2NFPanel);
        tabbedPane.addTab("3NF", view3NFPanel);
        
        // Add tabbed pane to frame
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        
        // Add a refresh button
        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.addActionListener(e -> refreshAllPanels());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
    }
    
    private void refreshAllPanels() {
        rawDataPanel.loadData();
        view1NFPanel.loadData();
        view2NFPanel.loadData();
        view3NFPanel.loadData();
    }
}