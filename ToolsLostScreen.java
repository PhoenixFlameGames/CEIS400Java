package ToolManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

/**
 * Report Lost Tools - Display checked-out tools and send info to ApiClient to report them as lost.
 * 
 * @author 
 */
public class ToolsLostScreen extends JPanel {
    private JList<Tool> toolList;
    private DefaultListModel<Tool> toolListModel;
    private JTextField toolIDField;
    private JTextField toolCountField;
    private Main app;
    private ApiClient clientAPI;
    private String employeeName;

    public ToolsLostScreen(Main app, ApiClient clientAPI) {
        this.app = app;
        this.clientAPI = clientAPI;
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

        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2));
        fieldsPanel.add(new JLabel("Tool ID:"));
        fieldsPanel.add(toolIDField);
        fieldsPanel.add(new JLabel("Tool Count:"));
        fieldsPanel.add(toolCountField);

        JButton backButton = createStyledButton("Back", e -> app.showScreen("Main Menu"));
        JButton reportLostButton = createStyledButton("Report Lost", e -> reportLost());

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonsPanel.add(reportLostButton);
        buttonsPanel.add(backButton);

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
                // Fetch the list of active checkouts from the API
                Map<String, Object> responseMap = clientAPI.getActiveCheckoutsParsed();
                
                // Assuming response contains a list of tools
                List<Tool> tools = (List<Tool>) responseMap.get("tools"); // Adjust as necessary based on actual response structure
                
                toolListModel.clear();
                if (tools != null) {
                    for (Tool tool : tools) {
                        toolListModel.addElement(tool);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading checked-out tools: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    private void reportLost() {
        Tool selectedTool = toolList.getSelectedValue();
        if (selectedTool != null) {
            try {
                Employee currentEmployee = app.getCurrentEmployee();
                if (currentEmployee != null) {
                    int toolID = selectedTool.getToolID();

                    // Call API to report the tool as lost
                    Map<String, Object> responseMap = clientAPI.reportLostToolParsed(toolID);
                    
                    // Check if API response indicates success
                    if (responseMap.containsKey("success") && (Boolean) responseMap.get("success")) {
                        JOptionPane.showMessageDialog(this, "Tool reported as lost successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadCheckedOutTools(); // Reload the list of checked-out tools
                    } else {
                        JOptionPane.showMessageDialog(this, "Error reporting the tool as lost.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Employee ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error reporting the tool as lost: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
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