package ToolManagementSystem;

public class Employee {
    private int emp_id;
    private String emp_first_name;
    private String emp_last_name;
    private String emp_job_title;
    private String emp_start_date;
    private boolean emp_checkout_indicator;
    private String emp_user_name; // Updated to match login
    private String emp_password;  // Updated to match login

    // Getters and Setters
    public int getEmpId() {
        return emp_id;
    }

    public void setEmpId(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmpFirstName() {
        return emp_first_name;
    }

    public void setEmpFirstName(String emp_first_name) {
        this.emp_first_name = emp_first_name;
    }

    public String getEmpLastName() {
        return emp_last_name;
    }

    public void setEmpLastName(String emp_last_name) {
        this.emp_last_name = emp_last_name;
    }

    public String getEmpJobTitle() {
        return emp_job_title;
    }

    public void setEmpJobTitle(String emp_job_title) {
        this.emp_job_title = emp_job_title;
    }

    public String getEmpStartDate() {
        return emp_start_date;
    }

    public void setEmpStartDate(String emp_start_date) {
        this.emp_start_date = emp_start_date;
    }

    public boolean isEmpCheckoutIndicator() {
        return emp_checkout_indicator;
    }

    public void setEmpCheckoutIndicator(boolean emp_checkout_indicator) {
        this.emp_checkout_indicator = emp_checkout_indicator;
    }

    public String getUsername() {  // Renamed getter for consistency with login code
        return emp_user_name;
    }

    public void setUsername(String emp_user_name) {
        this.emp_user_name = emp_user_name;
    }

    public String getPassword() {  // Renamed getter for consistency with login code
        return emp_password;
    }

    public void setPassword(String emp_password) {
        this.emp_password = emp_password;
    }

    @Override
    public String toString() {
        return emp_first_name + " " + emp_last_name;
    }
}