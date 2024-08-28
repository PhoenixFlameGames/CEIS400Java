package ToolManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Login Screen - Passes username and password by user to ApiClient for authentication.
 * On successful authentication, sets the Employee object in Main.
 */
public class LoginScreen extends JPanel {
    private final Main app;
    private final ApiClient clientApi;

    // Constructor
    public LoginScreen(Main app, ApiClient clientApi) {
        this.app = app;
        this.clientApi = clientApi;
        
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
                try {
                    Map<String, Object> response = clientApi.loginEmployeeParsed(username, password);
                    boolean success = (boolean) response.get("success");

                    if (success) {
                        // Assuming response contains employee details if authentication is successful
                        Employee employee = new Employee(
                                (int) response.get("emp_ID"),
                                (String) response.get("first_name"),
                                (String) response.get("last_name"),
                                (String) response.get("username"),
                                null // Password not needed to be stored in the Employee object
                        );
                        app.setCurrentEmployee(employee); // Set the current employee
                        app.showScreen("Main Menu");
                    } else {
                        JOptionPane.showMessageDialog(LoginScreen.this, response.get("message"), "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginScreen.this, "An error occurred while authenticating. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
    }
}