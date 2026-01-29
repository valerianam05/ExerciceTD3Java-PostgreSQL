package model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Integer id;
    private String reference;
    private Instant creationDatetime;
    private List<DishOrder> dishOrderList = new ArrayList<>();

    public Order() {
        this.creationDatetime = Instant.now();
    }

    public double getTotalAmountWithoutVAT() {
        double total = 0;
        for (DishOrder item : dishOrderList) {
            // On multiplie le prix du plat par la quantité
            if (item.getDish().getPrice() != null) {
                total += item.getDish().getPrice() * item.getQuantity();
            }
        }
        return total;
    }

    public double getTotalAmountWithVAT() {
        // Application d'une TVA à 20% par exemple
        return getTotalAmountWithoutVAT() * 1.20;
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public Instant getCreationDatetime() { return creationDatetime; }
    public void setCreationDatetime(Instant creationDatetime) { this.creationDatetime = creationDatetime; }

    public List<DishOrder> getDishOrderList() { return dishOrderList; }
    public void setDishOrderList(List<DishOrder> dishOrderList) { this.dishOrderList = dishOrderList; }
}
