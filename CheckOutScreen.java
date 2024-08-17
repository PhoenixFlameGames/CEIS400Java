package ToolManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Check-Out Tools - Display all available tools, allow tool selection, send checkout info to ClientAPI
 * 
 * @author mbailey
 */
class CheckOutScreen extends JPanel {
    private JList<Tool> toolList;
    private DefaultListModel<Tool> toolListModel;
    private JTextField toolIDField;
    private JTextField toolCountField;
    private Main app;
    private ClientAPI clientAPI;

    public CheckOutScreen(Main app, ClientAPI clientAPI) {
        this.app = app;
        this.clientAPI = clientAPI;

        setLayout(new BorderLayout());
        setBackground(Color.decode("#e0f7fa"));

        toolListModel = new DefaultListModel<>();
        toolList = new JList<>(toolListModel);
        toolList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        toolIDField = new JTextField(10);
        toolIDField.setEditable(false);
        toolCountField = new JTextField(10);
        toolCountField.setEditable(false);

        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2));
        fieldsPanel.add(new JLabel("Tool ID:"));
        fieldsPanel.add(toolIDField);
        fieldsPanel.add(new JLabel("Tool Count:"));
        fieldsPanel.add(toolCountField);

        JButton backButton = createStyledButton("Back", e -> app.showScreen("Main Menu"));
        JButton checkOutButton = createStyledButton("Check Out", e -> checkOutTool());

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonsPanel.add(checkOutButton);
        buttonsPanel.add(backButton);

        add(new JScrollPane(toolList), BorderLayout.CENTER);
        add(fieldsPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);

        toolList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Tool selectedTool = toolList.getSelectedValue();
                if (selectedTool != null) {
                    toolIDField.setText(String.valueOf(selectedTool.getToolID()));
                    toolCountField.setText(String.valueOf(selectedTool.getToolCount()));
                }
            }
        });

        loadTools();
    }

    private void loadTools() {
        try {
            List<Tool> tools = clientAPI.getInventory();
            toolListModel.clear();
            for (Tool tool : tools) {
                if (!tool.isToolOutOfStockIndicator()) {
                    toolListModel.addElement(tool);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading tools from server.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();  // Optional: Print stack trace for debugging
        }
    }

    private void checkOutTool() {
        Tool selectedTool = toolList.getSelectedValue();
        if (selectedTool != null) {
            try {
                Employee currentEmployee = app.getCurrentEmployee();
                if (currentEmployee != null) {
                    int empID = currentEmployee.getEmpId();
                    int newCount = selectedTool.getToolCount() - 1;
                    boolean outOfStock = (newCount <= 0);

                    if (outOfStock) {
                        selectedTool.setToolOutOfStockIndicator(true);
                    }
                    selectedTool.setToolCount(newCount);

                    boolean success = clientAPI.sendTransaction(empID, selectedTool.getToolID(), "Checked Out");
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Tool checked out successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadTools();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error checking out tool.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error processing checkout.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();  // Optional: Print stack trace for debugging
            }
        } else {
            JOptionPane.showMessageDialog(this, "No Tool Selected", "Error", JOptionPane.ERROR_MESSAGE);
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