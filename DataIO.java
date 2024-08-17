package ToolManagementSystem;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Input/Output class for interacting with the database.
 */
public class DataIO {

    // Constants
    private final String DATABASE_NAME = "NAME"; // Replace with actual database name
    private final String CONNECTION_STRING = "jdbc:mysql://0.0.0.0:8000/" + DATABASE_NAME; // Verify connection info
    private final String USER_NAME = "USERNAME"; // Replace with valid username
    private final String PASSWORD = "PASSWORD"; // Replace with valid password

    // Method to get a connection to the database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING, USER_NAME, PASSWORD);
    }

    // Method to authenticate user
    public boolean authenticateUser(String username, String password) {
        // Use when able to authenticate
        /*
        String query = "SELECT COUNT(*) FROM employees WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); 

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Return true if user exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
        */
        return true; // remove this line when able to authenticate
    }
    
    // Method to fetch all tools from the database
    public List<Tool> fetchTools() {
        List<Tool> tools = new ArrayList<>();
        String query = "SELECT tool_id, tool_name, tool_type, tool_added_to_inventory_date, tool_cost, tool_count, tool_out_of_stock_indicator FROM tools";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Tool tool = new Tool(
                        rs.getInt("tool_id"),
                        rs.getString("tool_name"),
                        rs.getString("tool_type"),
                        rs.getDate("tool_added_to_inventory_date").toLocalDate(),
                        rs.getDouble("tool_cost"),
                        rs.getInt("tool_count"),
                        rs.getBoolean("tool_out_of_stock_indicator")
                );
                tools.add(tool);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tools;
    }

    // Method to get details of a specific tool
    public Tool getToolDetails(int toolID) {
        String query = "SELECT tool_id, tool_name, tool_type, tool_added_to_inventory_date, tool_cost, tool_count, tool_out_of_stock_indicator FROM tools WHERE tool_id = ?";
        Tool tool = null;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, toolID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tool = new Tool(
                            rs.getInt("tool_id"),
                            rs.getString("tool_name"),
                            rs.getString("tool_type"),
                            rs.getDate("tool_added_to_inventory_date").toLocalDate(),
                            rs.getDouble("tool_cost"),
                            rs.getInt("tool_count"),
                            rs.getBoolean("tool_out_of_stock_indicator")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tool;
    }

    // Method to get checked-out tools by employee
    public List<Tool> getCheckedOutTools(String employeeName) {
        List<Tool> tools = new ArrayList<>();
        String query = "SELECT tool_id, tool_name, tool_type, tool_added_to_inventory_date, tool_cost, tool_count, tool_out_of_stock_indicator FROM tools WHERE checked_to = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, employeeName);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Tool tool = new Tool(
                            rs.getInt("tool_id"),
                            rs.getString("tool_name"),
                            rs.getString("tool_type"),
                            rs.getDate("tool_added_to_inventory_date").toLocalDate(),
                            rs.getDouble("tool_cost"),
                            rs.getInt("tool_count"),
                            rs.getBoolean("tool_out_of_stock_indicator")
                    );
                    tools.add(tool);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tools;
    }
    
    /** Method to check out a tool and log the transaction
     * 
     * @param toolID
     * @param employeeName
     * @param transactionType
     * @return
     * 
     * Updates ToolSchema: reduce count by 1 and sets tool_out_of_stock_indicator to TRUE if count reaches 0
     * Updates CheckInOutSchema: by inserting  
     */
    public boolean checkOutTool(int emp_ID, String employeeName, int toolID, String transactionType) {
        String updateToolQuery = "UPDATE tools SET tool_count = tool_count - 1, tool_out_of_stock_indicator = (tool_count = 0) WHERE tool_id = ?";
        String insertTransactionQuery = "INSERT INTO check_in_out (emp_ID, employee_name, tool_ID, transaction_type, transaction_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // Update tool details
            try (PreparedStatement pstmtUpdateTool = conn.prepareStatement(updateToolQuery)) {
                pstmtUpdateTool.setInt(1, toolID);
                int affectedRowsTool = pstmtUpdateTool.executeUpdate();

                if (affectedRowsTool == 0) {
                    conn.rollback();
                    return false;
                }

                // Insert transaction record
                try (PreparedStatement pstmtInsertTransaction = conn.prepareStatement(insertTransactionQuery)) {
                    pstmtInsertTransaction.setInt(1, emp_ID);
                    pstmtInsertTransaction.setString(2, employeeName);
                    pstmtInsertTransaction.setInt(3, toolID);
                    pstmtInsertTransaction.setString(4, transactionType);
                    pstmtInsertTransaction.setDate(5, Date.valueOf(LocalDate.now()));
                    int affectedRowsTransaction = pstmtInsertTransaction.executeUpdate();

                    if (affectedRowsTransaction > 0) {
                        conn.commit();
                        return true;
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Method to check in a tool and log the transaction
     * 
     * @param toolID
     * @param employeeName
     * @param transactionType
     * @return
     * 
     * Updates ToolSchema: reduce count by 1 and sets tool_out_of_stock_indicator to TRUE if count reaches 0
     * Updates CheckInOutSchema: by inserting  
     */
    public boolean checkInTool(int emp_ID, String employeeName, int toolID, String transactionType) {
        String updateToolQuery = "UPDATE tools SET tool_count = tool_count + 1, tool_out_of_stock_indicator = (tool_count + 1 <= 0) WHERE tool_id = ?";
        String insertTransactionQuery = "INSERT INTO check_in_out (emp_ID, employee_name, tool_ID, transaction_type, transaction_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // Update tool details
            try (PreparedStatement pstmtUpdateTool = conn.prepareStatement(updateToolQuery)) {
                pstmtUpdateTool.setInt(1, toolID);
                int affectedRowsTool = pstmtUpdateTool.executeUpdate();

                if (affectedRowsTool == 0) {
                    conn.rollback();
                    return false;
                }

                // Insert transaction record
                try (PreparedStatement pstmtInsertTransaction = conn.prepareStatement(insertTransactionQuery)) {
                    pstmtInsertTransaction.setInt(1, emp_ID);
                    pstmtInsertTransaction.setString(2, employeeName);
                    pstmtInsertTransaction.setInt(3, toolID);
                    pstmtInsertTransaction.setString(4, transactionType);
                    pstmtInsertTransaction.setDate(5, Date.valueOf(LocalDate.now()));
                    int affectedRowsTransaction = pstmtInsertTransaction.executeUpdate();

                    if (affectedRowsTransaction > 0) {
                        conn.commit();
                        return true;
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean reportToolLost(int toolID, String employeeName, String transactionType) {
        String updateToolQuery = "UPDATE tools SET tool_count = tool_count - 1 WHERE tool_id = ?";
        String insertTransactionQuery = "INSERT INTO transactions (transaction_item_id, transaction_owner_name, transaction_type, transaction_open_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtUpdateTool = conn.prepareStatement(updateToolQuery)) {
                pstmtUpdateTool.setInt(1, toolID);
                int affectedRowsTool = pstmtUpdateTool.executeUpdate();

                if (affectedRowsTool == 0) {
                    conn.rollback();
                    return false;
                }

                try (PreparedStatement pstmtInsertTransaction = conn.prepareStatement(insertTransactionQuery)) {
                    pstmtInsertTransaction.setInt(1, toolID);
                    pstmtInsertTransaction.setString(2, employeeName);
                    pstmtInsertTransaction.setString(3, transactionType);
                    pstmtInsertTransaction.setDate(4, Date.valueOf(LocalDate.now()));
                    int affectedRowsTransaction = pstmtInsertTransaction.executeUpdate();

                    if (affectedRowsTransaction > 0) {
                        conn.commit();
                        return true;
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Method to get details of a specific employee
    public Employee getEmployeeDetails(String username) {
        String query = "SELECT emp_id, emp_first_name, emp_last_name, emp_job_title, emp_start_date, emp_checkout_indicator FROM employees WHERE username = ?";
        Employee employee = null;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    employee = new Employee(
                            rs.getInt("emp_id"),
                            rs.getString("emp_first_name"),
                            rs.getString("emp_last_name"),
                            rs.getString("emp_job_title"),
                            rs.getDate("emp_start_date") != null ? rs.getDate("emp_start_date").toLocalDate() : null,
                            rs.getBoolean("emp_checkout_indicator")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }
}