import java.util.ArrayList;
import java.util.List;

public class Dish {
    private int id;
    private String name;
    private Double price;
    private List<DishIngredient> recipe = new ArrayList<>();

    public void setRecipe(List<DishIngredient> recipe) {
        this.recipe = recipe;
    }

    public List<DishIngredient> getRecipe() {
        return recipe;
    }

    public void addIngredientToRecipe(DishIngredient di) {
        this.recipe.add(di);
    }

    public double getDishCost() {
        double total = 0;
        for (DishIngredient line : recipe) {
            total += line.getLineCost();
        }
        return total;
    }

    public double getGrossMargin() {
        if (this.price == null) throw new RuntimeException("Prix de vente non défini");
        return this.price - getDishCost();
    }

    // Getters et Setters pour id, name, price
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}