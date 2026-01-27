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
    private List<StockMovement> stockMovementList = new ArrayList<>();

    public Ingredient() {
    }

    // CONSTRUCTEUR CORRIGÉ (avec catégorie)
    public Ingredient(Integer id, String name, Double price, CategoryEnum category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    /**
     * Calcule l'état du stock à un instant précis T
     */
    public StockValue getStockValueAt(Instant t) {
        double totalQuantity = 0.0;
        Unit defaultUnit = Unit.KG; // Unité par défaut au cas où la liste est vide

        for (StockMovement m : this.stockMovementList) {
            // Mise à jour de l'unité pour être cohérent avec les mouvements réels
            defaultUnit = m.getValue().getUnit();

            if (!m.getCreationDatetime().isAfter(t)) {
                if (m.getType() == MovementType.IN) {
                    totalQuantity += m.getValue().getQuantity();
                } else if (m.getType() == MovementType.OUT) {
                    totalQuantity -= m.getValue().getQuantity();
                }
            }
        }
        return new StockValue(totalQuantity, defaultUnit);
    }

    /**
     * Renvoie le stock actuel (à l'instant présent)
     */
    public double getCurrentStock() {
        // On réutilise intelligemment la méthode temporelle
        return getStockValueAt(Instant.now()).getQuantity();
    }

    // --- GETTERS ET SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public CategoryEnum getCategory() { return category; }
    public void setCategory(CategoryEnum category) { this.category = category; }

    public List<StockMovement> getStockMovementList() { return stockMovementList; }
    public void setStockMovementList(List<StockMovement> mouvements) {
        this.stockMovementList = mouvements;
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