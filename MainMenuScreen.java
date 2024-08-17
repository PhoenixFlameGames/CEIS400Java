package ToolManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Main Menu Screen - Displays the main menu options for the tool management system.
 */
public class MainMenuScreen extends JPanel {
    private JLabel welcomeLabel;

    public MainMenuScreen(Main app) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Set background color
        setBackground(Color.decode("#e0f7fa"));

        welcomeLabel = new JLabel("Welcome, ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(welcomeLabel, gbc);

        // Create and configure buttons
        JButton checkOutButton = createButton("Check-Out", e -> app.showScreen("Check-Out"));
        JButton checkInButton = createButton("Check-In", e -> app.showScreen("Check-In"));
        JButton toolsLostButton = createButton("Tools Lost", e -> app.showScreen("Tools Lost"));
        JButton toolsRefillButton = createButton("Tools Refill", e -> app.showScreen("Tools Refill"));
        JButton inventoryListButton = createButton("Inventory List", e -> app.showScreen("Inventory Screen"));

        // Adding buttons to the panel
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(checkOutButton, gbc);

        gbc.gridx = 1;
        add(checkInButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(toolsLostButton, gbc);

        gbc.gridx = 1;
        add(toolsRefillButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(inventoryListButton, gbc);
    }

    private JButton createButton(String text, ActionListener listener) {
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

    public void updateWelcomeLabel(String employeeName) {
        welcomeLabel.setText("Welcome, " + employeeName);
    }
}