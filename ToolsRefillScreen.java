package ToolManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

/**
 * Tools Refill Screen - Display all tools and provide an option to order a refill.
 */
public class ToolsRefillScreen extends JPanel {
    private Main app;
    private JList<Tool> toolList;
    private DefaultListModel<Tool> toolListModel;
    private ApiClient clientAPI;

    public ToolsRefillScreen(Main app, ApiClient clientAPI) {
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

    private void loadTools() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Fetch the list of out-of-stock tools from the API
                Map<String, Object> responseMap = clientAPI.getOutOfStockMaterialsParsed();
                
                // Assuming response contains a list of tools
                List<Tool> tools = (List<Tool>) responseMap.get("tools"); // Adjust as necessary based on actual response structure
                
                toolListModel.clear();
                if (tools != null) {
                    for (Tool tool : tools) {
                        toolListModel.addElement(tool);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading tools: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    private boolean orderRefill(int toolID) {
        try {
            // Call API to order the refill for the tool
            Map<String, Object> responseMap = clientAPI.markToolAsReplacedParsed(toolID);
            
            // Check if API response indicates success
            return responseMap.containsKey("success") && (Boolean) responseMap.get("success");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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