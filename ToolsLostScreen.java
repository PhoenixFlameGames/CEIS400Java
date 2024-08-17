package ToolManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Tools Lost Screen - Display checked-out tools and report lost tools.
 * 
 * @author mbailey
 */
public class ToolsLostScreen extends JPanel {
    private JList<Tool> toolList;
    private DefaultListModel<Tool> listModel;
    private Main app;
    private String employeeName;

    public ToolsLostScreen(Main app) {
        this.app = app;
        setLayout(new BorderLayout());
        
        // Set background color
        setBackground(Color.decode("#e0f7fa"));

        JLabel label = new JLabel("Tools Checked Out", SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        toolList = new JList<>(listModel);
        add(new JScrollPane(toolList), BorderLayout.CENTER);

        // Create buttons with matching style
        JButton reportLostButton = createStyledButton("Report Lost", e -> reportLostTool());
        JButton backButton = createStyledButton("Back", e -> app.showScreen("Main Menu"));

        // Create button panel with GridLayout for equal button sizes
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonPanel.add(reportLostButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
        loadTools();
    }

    private void loadTools() {
        DataIO dataIO = new DataIO();
        List<Tool> tools = dataIO.getCheckedOutTools(employeeName);
        listModel.clear();
        for (Tool tool : tools) {
            listModel.addElement(tool);
        }
    }

    private void reportLostTool() {
        Tool selectedTool = toolList.getSelectedValue();
        if (selectedTool != null) {
            DataIO dataIO = new DataIO();
            boolean success = dataIO.reportToolLost(selectedTool.getToolID(), employeeName, "Lost");
            if (success) {
                JOptionPane.showMessageDialog(this, "Tool reported lost successfully.");
                loadTools();
            } else {
                JOptionPane.showMessageDialog(this, "Error reporting tool lost.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a tool to report lost.", "No Tool Selected", JOptionPane.WARNING_MESSAGE);
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