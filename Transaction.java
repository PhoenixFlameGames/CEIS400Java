package ToolManagementSystem;

import java.util.Date;

public class Transaction {
    private int transaction_id; // Added for uniqueness
    private int transaction_owner_id;
    private int transaction_item_id;
    private String transaction_type;
    private String transaction_status; // Added for status tracking
    private Date transaction_open_date;
    private Date transaction_close_date; // Optional, if you need to record when a transaction is closed

    // Default constructor
    public Transaction() {
    }

    // Parameterized constructor
    public Transaction(int transaction_id, int transaction_owner_id, int transaction_item_id, String transaction_type, String transaction_status, Date transaction_open_date, Date transaction_close_date) {
        this.transaction_id = transaction_id;
        this.transaction_owner_id = transaction_owner_id;
        this.transaction_item_id = transaction_item_id;
        this.transaction_type = transaction_type;
        this.transaction_status = transaction_status;
        this.transaction_open_date = transaction_open_date;
        this.transaction_close_date = transaction_close_date;
    }

    // Getters and Setters
    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getTransaction_owner_id() {
        return transaction_owner_id;
    }

    public void setTransaction_owner_id(int transaction_owner_id) {
        this.transaction_owner_id = transaction_owner_id;
    }

    public int getTransaction_item_id() {
        return transaction_item_id;
    }

    public void setTransaction_item_id(int transaction_item_id) {
        this.transaction_item_id = transaction_item_id;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    public Date getTransaction_open_date() {
        return transaction_open_date;
    }

    public void setTransaction_open_date(Date transaction_open_date) {
        this.transaction_open_date = transaction_open_date;
    }

    public Date getTransaction_close_date() {
        return transaction_close_date;
    }

    public void setTransaction_close_date(Date transaction_close_date) {
        this.transaction_close_date = transaction_close_date;
    }

    // Optional: Override toString() for better debugging output
    @Override
    public String toString() {
        return "Transaction{" +
               "transaction_id=" + transaction_id +
               ", transaction_owner_id=" + transaction_owner_id +
               ", transaction_item_id=" + transaction_item_id +
               ", transaction_type='" + transaction_type + '\'' +
               ", transaction_status='" + transaction_status + '\'' +
               ", transaction_open_date=" + transaction_open_date +
               ", transaction_close_date=" + transaction_close_date +
               '}';
    }
}