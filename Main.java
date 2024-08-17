package ToolManagementSystem;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String employeeName;
    private int empID;
    private MainMenuScreen mainMenuScreen;
    private CheckInScreen checkInScreen;
    private ToolsLostScreen toolsLostScreen;
    private InventoryScreen inventoryScreen;
    private ToolsRefillScreen toolsRefillScreen;

    public Main() {
        setTitle("Tool Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize screens
        mainMenuScreen = new MainMenuScreen(this);
        checkInScreen = new CheckInScreen(this);
        toolsLostScreen = new ToolsLostScreen(this);
        inventoryScreen = new InventoryScreen(this);
        toolsRefillScreen = new ToolsRefillScreen(this);

        // Adding all screens to the card layout
        mainPanel.add(new LoginScreen(this), "Login");
        mainPanel.add(mainMenuScreen, "Main Menu");
        mainPanel.add(new CheckOutScreen(this), "Check-Out");
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
                mainMenuScreen.updateWelcomeLabel(employeeName);
                break;
            case "Check-In":
                checkInScreen.setEmployeeName(employeeName);
                break;
            case "Tools Lost":
                toolsLostScreen.setEmployeeName(employeeName);
                break;
            case "Inventory Screen":
                inventoryScreen.loadTools();
                break;
            case "Tools Refill":
                toolsRefillScreen.loadLostTools();
                break;
            default:
                break;
        }
        cardLayout.show(mainPanel, screenName);
    }

    public void setEmployeeName(String name) {
        this.employeeName = name;
    }


    public String getEmployeeName() {
        return employeeName;
    }
    
    public void setEmpID(int empID) {
        this.empID = empID;
    }
    
    public int getEmpID() {
        return empID;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main(); // Instantiate Main
            app.setVisible(true);
        });
    }
}