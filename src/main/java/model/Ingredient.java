package model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ingredient {
    private Integer id;
    private String name;
    private CategoryEnum category;
    private Double price;
    private Unit unit;
    private List<StockMovement> stockMovements = new ArrayList<>();

    public Ingredient() {
    }

    public Ingredient(Integer id, String name, Double price, Unit unit, CategoryEnum category, List<StockMovement> stockMovements) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.category = category;
        this.stockMovements = (stockMovements != null) ? stockMovements : new ArrayList<>();

    }

    public StockValue getStockValueAt(Instant t) {
        double totalQuantity = 0.0;
        // On utilise l'unité de l'ingrédient définie dans la classe
        Unit currentUnit = (this.unit != null) ? this.unit : Unit.KG;

        if (this.stockMovements != null) {
            for (StockMovement m : this.stockMovements) {
                // On vérifie si le mouvement a eu lieu AVANT ou À l'instant t
                if (!m.getCreationDatetime().isAfter(t)) {
                    if (m.getType() == MovementType.IN) {
                        totalQuantity += m.getValue().getQuantity();
                    } else if (m.getType() == MovementType.OUT) {
                        totalQuantity -= m.getValue().getQuantity();
                    }
                }
            }
        }
        return new StockValue(totalQuantity, currentUnit);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public CategoryEnum getCategory() { return category; }
    public void setCategory(CategoryEnum category) { this.category = category; }

    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }

    public List<StockMovement> getStockMovements() { return stockMovements; }
    public void setStockMovements(List<StockMovement> stockMovements) {
        this.stockMovements = stockMovements;
    }


    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}