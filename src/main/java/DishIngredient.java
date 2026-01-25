public class DishIngredient {
    private Ingredient ingredient;
    private double quantity;
    private Unit unit;
    //id dish
    public DishIngredient(Ingredient ingredient, double quantity, Unit unit) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
    }

    public double getLineCost() {
        return ingredient.getPrice() * quantity;
    }

    // Getters
    public Ingredient getIngredient() { return ingredient; }
    public double getQuantity() { return quantity; }
    public Unit getUnit() { return unit; }
}
