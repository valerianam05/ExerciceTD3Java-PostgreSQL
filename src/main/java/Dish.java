import java.util.ArrayList;
import java.util.List;

public class Dish {
    private int id;
    private String name;
    private Double price;
    // La liste s'appelle 'recipe' (recette)
    private List<DishIngredient> recipe = new ArrayList<>();

    // --- LE SETTER (pour corriger l'erreur ligne 21) ---
    public void setRecipe(List<DishIngredient> recipe) {
        this.recipe = recipe;
    }

    // --- LE GETTER (pour corriger l'erreur ligne 77) ---
    public List<DishIngredient> getRecipe() {
        return recipe;
    }

    // Méthode utilitaire pour ajouter un ingrédient un par un
    public void addIngredientToRecipe(DishIngredient di) {
        this.recipe.add(di);
    }

    // Calcul du coût total
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