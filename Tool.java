package ToolManagementSystem;

import org.json.JSONObject;

public class Tool {
    private int toolID;
    private String toolName;
    private String toolType;
    private int toolCount;
    private boolean outOfStockIndicator;

    // Constructor
    public Tool(int toolID, String toolName, String toolType, int toolCount, boolean outOfStockIndicator) {
        this.toolID = toolID;
        this.toolName = toolName;
        this.toolType = toolType;
        this.toolCount = toolCount;
        this.outOfStockIndicator = outOfStockIndicator;
    }

    // Getters and Setters
    public int getToolID() {
        return toolID;
    }

    public void setToolID(int toolID) {
        this.toolID = toolID;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    public int getToolCount() {
        return toolCount;
    }

    public void setToolCount(int toolCount) {
        this.toolCount = toolCount;
    }

    public boolean isOutOfStockIndicator() {
        return outOfStockIndicator;
    }

    public void setOutOfStockIndicator(boolean outOfStockIndicator) {
        this.outOfStockIndicator = outOfStockIndicator;
    }

    // Method to create a Tool object from a JSONObject
    public static Tool fromJson(JSONObject jsonObject) {
        int toolID = jsonObject.getInt("item_id");
        String toolName = jsonObject.getString("item_type");
        int toolCount = jsonObject.getInt("item_stock");
        boolean outOfStockIndicator = jsonObject.getBoolean("item_out_of_stock_indicator");
        
        return new Tool(toolID, toolName, toolName, toolCount, outOfStockIndicator);
    }
}