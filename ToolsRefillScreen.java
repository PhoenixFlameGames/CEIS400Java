package ToolManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Tools Refill Screen - Display all tools and provide an option to order a refill.
 */
public class ToolsRefillScreen extends JPanel {
    private Main app;
    private JList<Tool> toolList;
    private DefaultListModel<Tool> toolListModel;
    private ClientAPI clientAPI;

    public ToolsRefillScreen(Main app, ClientAPI clientAPI) {
        this.app = app;
        this.clientAPI = clientAPI;

        setLayout(new BorderLayout());
        
        // Set background color
        setBackground(Color.decode("#e0f7fa"));

        toolListModel = new DefaultListModel<>();
        toolList = new JList<>(toolListModel);
        toolList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(toolList);

        // Create buttons with matching style
        JButton orderButton = createStyledButton("Order", e -> {
            Tool selectedTool = toolList.getSelectedValue();
            if (selectedTool != null) {
                // Process the refill order
                if (orderRefill(selectedTool.getToolID())) {
                    JOptionPane.showMessageDialog(this, "Order completed", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Order failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No tool selected.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton backButton = createStyledButton("Back", e -> app.showScreen("Main Menu"));

        // Create button panel with GridLayout for equal button sizes
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonPanel.add(orderButton);
        buttonPanel.add(backButton);

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load tools from ClientAPI
        loadTools();
    }

    public void loadTools() {
        toolListModel.clear();
        try {
            List<Tool> tools = clientAPI.getInventory();
            for (Tool tool : tools) {
                if (tool.isToolOutOfStockIndicator()) {
                    toolListModel.addElement(tool);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading tools from server.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();  // Optional: Print stack trace for debugging
        }
    }

    private boolean orderRefill(int toolID) {
        Tool selectedTool = toolList.getSelectedValue();
        if (selectedTool != null) {
            try {
                Employee currentEmployee = app.getCurrentEmployee();
                if (currentEmployee != null) {
                    int empID = currentEmployee.getEmpId();
                    int newCount = selectedTool.getToolCount() + 1;
                    boolean outOfStock = (newCount > 0);

                    if (outOfStock) {
                        selectedTool.setToolOutOfStockIndicator(false);
                    }
                    selectedTool.setToolCount(newCount);

                    boolean success = clientAPI.sendTransaction(empID, selectedTool.getToolID(), "Ordered");
                    if (success) { 
                        JOptionPane.showMessageDialog(this, "Tool ordered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadTools(); // Reload the list of tools
                    } else {
                        JOptionPane.showMessageDialog(this, "Error checking in the tool.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Employee ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error checking in the tool: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a checked-out tool.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return true;
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