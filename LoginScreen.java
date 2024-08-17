package ToolManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Login Screen - Passes username and password by user to ClientAPI for authentication
 * On successful authentication, sets the Employee object in Main
 */
public class LoginScreen extends JPanel {
    private final Main app;
    private final ClientAPI clientApi;

    // Constructor
    public LoginScreen(Main app, ClientAPI clientApi) {
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

/*                // Authenticate user
                try {
                    List<Employee> employees = clientApi.getEmployees();
                    Employee matchingEmployee = null;

                    // Check for matching username
                    for (Employee employee : employees) {
                        if (employee.getUsername().equals(username)) {
                            matchingEmployee = employee;
                            break;
                        }
                    }

                    if (matchingEmployee == null) {
                        JOptionPane.showMessageDialog(LoginScreen.this, "Incorrect User Name.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    } else if (!matchingEmployee.getPassword().equals(password)) {
                        JOptionPane.showMessageDialog(LoginScreen.this, "Incorrect Password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Log in Successful
                        app.setCurrentEmployee(matchingEmployee); // Set the current employee
                        app.showScreen("Main Menu");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginScreen.this, "An error occurred while authenticating. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
*/
            app.showScreen("Main Menu"); //skip authentication for front end testing
            }
        });
    }
}