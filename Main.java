package ToolManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Main class to initialize the application and manage screen transitions.
 * 
 * @author mbailey
 */
public class Main extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginScreen loginScreen;
    private MainMenuScreen mainMenuScreen;
    private CheckOutScreen checkOutScreen;
    private CheckInScreen checkInScreen;
    private ToolsLostScreen toolsLostScreen;
    private InventoryScreen inventoryScreen;
    private ToolsRefillScreen toolsRefillScreen;
    private ClientAPI clientAPI;
    private Employee currentEmployee; // Declare the currentEmployee

    public static void main(String[] args) {
        // Create the application instance
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }    

    public Main() {
        setTitle("Tool Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize ClientAPI
        clientAPI = new ClientAPI();

        // Initialize screens
        loginScreen = new LoginScreen(this, clientAPI);
        mainMenuScreen = new MainMenuScreen(this);
        checkOutScreen = new CheckOutScreen(this, clientAPI);
        checkInScreen = new CheckInScreen(this, clientAPI);
        toolsLostScreen = new ToolsLostScreen(this, clientAPI);
        inventoryScreen = new InventoryScreen(this, clientAPI);
        toolsRefillScreen = new ToolsRefillScreen(this, clientAPI);

        // Adding all screens to the card layout
        mainPanel.add(loginScreen, "Login");
        mainPanel.add(mainMenuScreen, "Main Menu");
        mainPanel.add(checkOutScreen, "Check-Out");
        mainPanel.add(checkInScreen, "Check-In");
        mainPanel.add(toolsLostScreen, "Tools Lost");
        mainPanel.add(toolsRefillScreen, "Tools Refill");
        mainPanel.add(inventoryScreen, "Inventory Screen");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    public void showScreen(String screenName) {
        switch (screenName) {
            case "Main Menu":
                break;
            case "Check-Out":
                break;
            case "Check-In":
                break;
            case "Tools Lost":
                break;
            case "Inventory Screen":
                inventoryScreen.loadTools();
                break;
            case "Tools Refill":
                toolsRefillScreen.loadTools();
                break;
            default:
                break;
        }
        cardLayout.show(mainPanel, screenName);
    }

    // Method to set the current employee
    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
    }

    // Method to get the current employee
    public Employee getCurrentEmployee() {
        return this.currentEmployee;
    }
}