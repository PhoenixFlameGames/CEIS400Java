package ToolManagementSystem;

import java.time.LocalDate;

/**
 * Employee class representing an employee in the system.
 */
public class Employee {
    private int empID;
    private String empFirstName;
    private String empLastName;
    private String empJobTitle;
    private LocalDate empStartDate;
    private boolean empCheckoutIndicator;

    /**
     *
     * @param empID                 ID of the employee
     * @param empFirstName          First name of the employee
     * @param empLastName           Last name of the employee
     * @param empJobTitle           Job title of the employee (optional)
     * @param empStartDate          Start date of the employee
     * @param empCheckoutIndicator  Indicator if the employee has checked out a tool
     */
    public Employee(int empID, String empFirstName, String empLastName, String empJobTitle, LocalDate empStartDate, boolean empCheckoutIndicator) {
        this.empID = empID;
        this.empFirstName = empFirstName;
        this.empLastName = empLastName;
        this.empJobTitle = empJobTitle;
        this.empStartDate = empStartDate;
        this.empCheckoutIndicator = empCheckoutIndicator;
    }

    // Getters and setters
    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        if (empID <= 0) throw new IllegalArgumentException("Employee ID must be positive.");
        this.empID = empID;
    }

    public String getEmpFirstName() {
        return empFirstName;
    }

    public void setEmpFirstName(String empFirstName) {
        this.empFirstName = empFirstName;
    }

    public String getEmpLastName() {
        return empLastName;
    }

    public void setEmpLastName(String empLastName) {
        this.empLastName = empLastName;
    }

    public String getEmpJobTitle() {
        return empJobTitle;
    }

    public void setEmpJobTitle(String empJobTitle) {
        this.empJobTitle = empJobTitle;
    }

    public LocalDate getEmpStartDate() {
        return empStartDate;
    }

    public void setEmpStartDate(LocalDate empStartDate) {
        this.empStartDate = empStartDate;
    }

    public boolean isEmpCheckoutIndicator() {
        return empCheckoutIndicator;
    }

    public void setEmpCheckoutIndicator(boolean empCheckoutIndicator) {
        this.empCheckoutIndicator = empCheckoutIndicator;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s %s, Job Title: %s, Start Date: %s, Checked Out Tool: %b",
                empID, empFirstName, empLastName, empJobTitle, empStartDate, empCheckoutIndicator);
    }
}