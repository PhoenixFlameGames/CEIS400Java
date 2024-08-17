package ToolManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Check-Out Tools - Display all available tools, allow tool selection, send checkout info to dataIO
 * 
 * @author mbailey
 */
class CheckOutScreen extends JPanel {
    private JList<Tool> toolList;
    private DefaultListModel<Tool> toolListModel;
    private JTextField toolIDField;
    private JTextField toolCountField;
    private Main app;
    private DataIO dataIO;

    public CheckOutScreen(Main app) {
        this.app = app;
        dataIO = new DataIO();

        setLayout(new BorderLayout());
        
        // Set background color
        setBackground(Color.decode("#e0f7fa"));

        // Initialize components
        toolListModel = new DefaultListModel<>();
        toolList = new JList<>(toolListModel);
        toolList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        toolIDField = new JTextField(10);
        toolIDField.setEditable(false);
        toolCountField = new JTextField(10);
        toolCountField.setEditable(false);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(2, 2));
        fieldsPanel.add(new JLabel("Tool ID:"));
        fieldsPanel.add(toolIDField);
        fieldsPanel.add(new JLabel("Tool Count:"));
        fieldsPanel.add(toolCountField);

        // Create buttons with matching style
        JButton backButton = createStyledButton("Back", e -> app.showScreen("Main Menu"));
        JButton checkOutButton = createStyledButton("Check Out", e -> checkOutTool());

        // Create button panel with GridLayout for equal button sizes
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonsPanel.add(checkOutButton);
        buttonsPanel.add(backButton);

        // Add components to the main panel
        add(new JScrollPane(toolList), BorderLayout.CENTER);
        add(fieldsPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);

        // Add list selection listener
        toolList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Tool selectedTool = toolList.getSelectedValue();
                if (selectedTool != null) {
                    toolIDField.setText(String.valueOf(selectedTool.getToolID()));
                    toolCountField.setText(String.valueOf(selectedTool.getToolCount()));
                }
            }
        });

        // Load tools from database
        loadTools();
    }

    private void loadTools() {
        List<Tool> tools = dataIO.fetchTools();
        toolListModel.clear();
        for (Tool tool : tools) {
            if (!tool.isToolOutOfStockIndicator()) {
                toolListModel.addElement(tool);
            }
        }
    }

    private void checkOutTool() {
        Tool selectedTool = toolList.getSelectedValue();
        if (selectedTool != null) {
            int empID = app.getEmpID();
            String employeeName = app.getEmployeeName();
            if (empID != -1) {
                boolean success = dataIO.checkOutTool(empID, employeeName, selectedTool.getToolID(), "Checked Out");
                if (success) {
                    JOptionPane.showMessageDialog(this, "Tool checked out successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadTools();
                } else {
                    JOptionPane.showMessageDialog(this, "Error checking out tool.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error: Employee ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an available tool.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createStyledButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 123, 255));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 100, 200), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 30));
        button.addActionListener(listener);
        return button;
    }
}