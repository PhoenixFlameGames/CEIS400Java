package ToolManagementSystem;

public class InventoryItem {
    private int item_id;
    private String item_type;
    private int item_stock;
    private int item_count;
    private boolean item_out_of_stock_indicator;
    private boolean item_lost_indicator;

    // Getters and setters
    @Override
    public String toString() {
        return item_type + " (ID: " + item_id + ")";
    }
}