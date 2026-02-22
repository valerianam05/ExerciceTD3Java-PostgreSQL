package model;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    private Integer id;
    private String name;
    private Double price;
    private DishTypeEnum dishType;
    private List<DishIngredient> dishIngredients = new ArrayList<>();

    public Dish() {
    }

    public Dish(Integer id, String name, Double price, DishTypeEnum dishType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.dishType = dishType;
    }

    public double getDishCost() {
        double totalCost = 0;
        if (dishIngredients != null) {
            for (DishIngredient di : dishIngredients) {
                if (di.getIngredient() != null && di.getIngredient().getPrice() != null) {
                    totalCost += di.getIngredient().getPrice() * di.getQuantity();
                }
            }
        }
        return totalCost;
    }


    public double getGrossMargin() {
        if (this.price == null) return 0;
        return this.price - getDishCost();
    }

    // --- GETTERS ET SETTERS ---
    // Note : On utilise 'getDishIngredients' pour correspondre à ton DataRetriever

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public DishTypeEnum getDishType() { return dishType; }
    public void setDishType(DishTypeEnum dishType) { this.dishType = dishType; }

    public List<DishIngredient> getDishIngredients() {
        return dishIngredients;
    }

    public void setDishIngredients(List<DishIngredient> dishIngredients) {
        this.dishIngredients = dishIngredients;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sellingPrice=" + price +
                ", cost=" + getDishCost() +
                ", margin=" + getGrossMargin() +
                '}';
    }
}