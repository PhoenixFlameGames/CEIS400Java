package ToolManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Inventory Screen - Display all tools and show detailed information when a tool is selected.
 */
public class InventoryScreen extends JPanel {
    private Main app;
    private JList<Tool> toolList;
    private DefaultListModel<Tool> toolListModel;
    private JLabel toolIdLabel, toolNameLabel, toolTypeLabel, toolAddedDateLabel, toolCostLabel, toolCountLabel, toolOutOfStockLabel;
    private ClientAPI clientAPI;

    public InventoryScreen(Main app, ClientAPI clientAPI) {
        this.app = app;
        this.clientAPI = clientAPI;

        setLayout(new BorderLayout());
        
        // Set background color
        setBackground(Color.decode("#e0f7fa"));

        toolListModel = new DefaultListModel<>();
        toolList = new JList<>(toolListModel);
        toolList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        toolList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Tool selectedTool = toolList.getSelectedValue();
                if (selectedTool != null) {
                    loadToolDetails(selectedTool);
                }
            }
        });

        JScrollPane listScrollPane = new JScrollPane(toolList);
        listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel detailPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        detailPanel.add(new JLabel("Tool ID:"));
        toolIdLabel = new JLabel();
        detailPanel.add(toolIdLabel);

        detailPanel.add(new JLabel("Tool Name:"));
        toolNameLabel = new JLabel();
        detailPanel.add(toolNameLabel);

        detailPanel.add(new JLabel("Tool Type:"));
        toolTypeLabel = new JLabel();
        detailPanel.add(toolTypeLabel);

        detailPanel.add(new JLabel("Added to Inventory Date:"));
        toolAddedDateLabel = new JLabel();
        detailPanel.add(toolAddedDateLabel);

        detailPanel.add(new JLabel("Tool Cost:"));
        toolCostLabel = new JLabel();
        detailPanel.add(toolCostLabel);

        detailPanel.add(new JLabel("Tool Count:"));
        toolCountLabel = new JLabel();
        detailPanel.add(toolCountLabel);

        detailPanel.add(new JLabel("Out of Stock Indicator:"));
        toolOutOfStockLabel = new JLabel();
        detailPanel.add(toolOutOfStockLabel);

        // Create buttons with matching style
        JButton backButton = createStyledButton("Back", e -> app.showScreen("Main Menu"));

        // Add components to the main panel
        add(listScrollPane, BorderLayout.NORTH);
        add(detailPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        // Set size constraints
        listScrollPane.setPreferredSize(new Dimension(800, 300));

        // Load tools from server
        loadTools();
    }

    public void loadTools() {
        SwingUtilities.invokeLater(() -> {
            try {
                List<Tool> tools = clientAPI.getInventory();
                toolListModel.clear();
                for (Tool tool : tools) {
                    toolListModel.addElement(tool);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading tools from server.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    private void loadToolDetails(Tool tool) {
        toolIdLabel.setText(String.valueOf(tool.getToolID()));
        toolNameLabel.setText(tool.getToolName());
        toolTypeLabel.setText(tool.getToolType());
        toolAddedDateLabel.setText(tool.getToolAddedToInventoryDate() != null ? tool.getToolAddedToInventoryDate().toString() : "N/A");
        toolCostLabel.setText(tool.getToolCost() != null ? String.format("%.2f", tool.getToolCost()) : "N/A");
        toolCountLabel.setText(String.valueOf(tool.getToolCount()));
        toolOutOfStockLabel.setText(tool.isToolOutOfStockIndicator() ? "Yes" : "No");
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