package ToolManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login Screen - Passes username and password by user to DataIO for authentication
 * On successful authentication, constructs an Employee object
 */
public class LoginScreen extends JPanel {
    // Constructor
    public LoginScreen(Main app) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Set background color
        setBackground(Color.decode("#e0f7fa"));

        // Title label
        JLabel titleLabel = new JLabel("GB Manufacturing Tool Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Username and Password fields
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        // Style the login button
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 100, 200), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add username field
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(userLabel, gbc);

        gbc.gridx = 1;
        add(userField, gbc);

        // Add password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passLabel, gbc);

        gbc.gridx = 1;
        add(passField, gbc);

        // Add login button
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(loginButton, gbc);

        // Add action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                // Authenticate user
                DataIO dataIO = new DataIO();
                boolean isAuthenticated = dataIO.authenticateUser(username, password);

                if (isAuthenticated) {
                    // Retrieve additional details to construct Employee object - REMOVE NOTE MARKS WHEN READY TO TEST
             //       Employee employee = dataIO.getEmployeeDetails(username);

                    // Set employee details in the app
             //       app.setEmpID(employee.getEmpID());
             //       app.setEmployeeName(employee.getEmpFirstName() + " " + employee.getEmpLastName());
                    app.showScreen("Main Menu");
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}