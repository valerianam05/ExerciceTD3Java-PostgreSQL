package model;

public class Stock {
    private String ingredientName;
    private double quantity;
    private Unit unit = Unit.KG;

    public Stock(String ingredientName, double quantity) {
        this.ingredientName = ingredientName;
        this.quantity = quantity;
    }

    public String getIngredientName() { return ingredientName; }
    public double getQuantity() { return quantity; }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return ingredientName + " : " + quantity + " " + unit;
    }
}
