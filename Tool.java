package ToolManagementSystem;

import java.time.LocalDate;

/**
 * Tool class representing a tool in the inventory.
 */
public class Tool {
    private int toolID;
    private String toolName;
    private String toolType;
    private LocalDate toolAddedToInventoryDate;
    private Double toolCost;
    private int toolCount;
    private boolean toolOutOfStockIndicator;

    /**
     * @param toolID                    ID of the tool
     * @param toolName                  Name of the tool
     * @param toolType                  Type of the tool (optional)
     * @param toolAddedToInventoryDate  Date when the tool was added to the inventory
     * @param toolCost                  Cost of the tool (optional)
     * @param toolCount                 Number of tools available in the inventory
     * @param toolOutOfStockIndicator   Indicator if the tool is out of stock
     */
    public Tool(int toolID, String toolName, String toolType, LocalDate toolAddedToInventoryDate, Double toolCost, int toolCount, boolean toolOutOfStockIndicator) {
        this.toolID = toolID;
        this.toolName = toolName;
        this.toolType = toolType;
        this.toolAddedToInventoryDate = toolAddedToInventoryDate;
        this.toolCost = toolCost;
        this.toolCount = toolCount;
        this.toolOutOfStockIndicator = toolOutOfStockIndicator;
    }

    // Getters and setters
    public int getToolID() {
        return toolID;
    }

    public void setToolID(int toolID) {
        if (toolID <= 0) throw new IllegalArgumentException("Tool ID must be positive.");
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

    public LocalDate getToolAddedToInventoryDate() {
        return toolAddedToInventoryDate;
    }

    public void setToolAddedToInventoryDate(LocalDate toolAddedToInventoryDate) {
        this.toolAddedToInventoryDate = toolAddedToInventoryDate;
    }

    public Double getToolCost() {
        return toolCost;
    }

    public void setToolCost(Double toolCost) {
        this.toolCost = toolCost;
    }

    public int getToolCount() {
        return toolCount;
    }

    public void setToolCount(int toolCount) {
        if (toolCount < 0) throw new IllegalArgumentException("Tool count cannot be negative.");
        this.toolCount = toolCount;
    }

    public boolean isToolOutOfStockIndicator() {
        return toolOutOfStockIndicator;
    }

    public void setToolOutOfStockIndicator(boolean toolOutOfStockIndicator) {
        this.toolOutOfStockIndicator = toolOutOfStockIndicator;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Type: %s, Added to Inventory: %s, Cost: %.2f, Count: %d, Out of Stock: %b",
                toolID, toolName, toolType, toolAddedToInventoryDate, toolCost, toolCount, toolOutOfStockIndicator);
    }
}