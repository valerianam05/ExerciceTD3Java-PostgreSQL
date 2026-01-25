public class StockValue {
    private double quantity;
    private Unit unit; // L'enum que tu as déjà (KG, L, etc.)

    public StockValue(double quantity, Unit unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public Unit getUnit() {
        return unit;
    }
}
