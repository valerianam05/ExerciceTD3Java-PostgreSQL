import java.util.List;

public class Dish {
    private Integer id;
    private String name;
    private Double price;
    private DishTypeEnum dishType;
    private List<Ingredient> ingredients;

    // Constructeur par défaut
    public Dish() {}

    public Double getDishCost() {
        double total = 0.0;
        if (ingredients != null) {
            for (Ingredient ing : ingredients) {
                // Important : ing.getQuantity() vient de la table de jointure
                if (ing.getPrice() != null && ing.getQuantity() != null) {
                    total += ing.getPrice() * ing.getQuantity();
                }
            }
        }
        return total;
    }

    public Double getGrossMargin() {
        if (this.price == null) {
            // Déclenché pour la Salade de fruits ou le Riz aux légumes
            throw new RuntimeException("Calcul de marge impossible : le prix de vente est manquant pour le plat : " + this.name);
        }
        return this.price - getDishCost();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    // La méthode qui manquait et causait l'erreur dans DataRetriever
    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", type=" + dishType +
                ", ingredientsCount=" + (ingredients != null ? ingredients.size() : 0) +
                '}';
    }
}