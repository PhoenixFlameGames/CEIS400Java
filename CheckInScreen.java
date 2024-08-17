package ToolManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Check-In Tools - Display tools and send info to DataIO to increase count
 * 
 * @author mbailey
 */
public class CheckInScreen extends JPanel {
    private JList<Tool> toolList;
    private DefaultListModel<Tool> toolListModel;
    private JTextField toolIDField;
    private Main app;
    private DataIO dataIO;
    private String employeeName;

    public CheckInScreen(Main app) {
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

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(1, 2));
        fieldsPanel.add(new JLabel("Tool ID:"));
        fieldsPanel.add(toolIDField);

        // Create buttons with matching style
        JButton backButton = createStyledButton("Back", e -> app.showScreen("Main Menu"));
        JButton checkInButton = createStyledButton("Check In", e -> checkInTool());

        // Create button panel with GridLayout for equal button sizes
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonsPanel.add(checkInButton);
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
                }
            }
        });

        // Load checked-out tools for the specific user
        loadCheckedOutTools();
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
        loadCheckedOutTools();
    }

    private void loadCheckedOutTools() {
        SwingUtilities.invokeLater(() -> {
            try {
                List<Tool> tools = dataIO.getCheckedOutTools(employeeName);
                toolListModel.clear();
                for (Tool tool : tools) {
                    toolListModel.addElement(tool);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading checked-out tools: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void checkInTool() {
        Tool selectedTool = toolList.getSelectedValue();
        if (selectedTool != null) {
            int empID = app.getEmpID(); // Retrieve the employee ID from the app
            String employeeName = app.getEmployeeName(); // Retrieve the employee name from the app

            if (empID != -1) { // Check if a valid employee ID is available
                boolean success = dataIO.checkInTool(empID, employeeName, selectedTool.getToolID(), "Checked In");
                if (success) { 
                    JOptionPane.showMessageDialog(this, "Tool checked in successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCheckedOutTools(); // Reload the list of checked-out tools
                } else {
                    JOptionPane.showMessageDialog(this, "Error checking in the tool.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error: Employee ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a checked-out tool.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createStyledButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE); // Text color
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