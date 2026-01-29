package model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Integer id;
    private String reference;
    private Instant creationDatetime;
    private String type;
    private String status;
    private Type_Order orderType;
    private List<DishOrder> dishOrderList = new ArrayList<>();

    public Order() {
        this.creationDatetime = Instant.now();
        this.type = "EAT_IN";
        this.status = "CREATED";
    }

    public static Object setTypeOrder(Type_Order typeOrder) {
        return typeOrder;
    }

    public Type_Order getOrderType() {
        return orderType;
    }

    public void setOrderType(Type_Order orderType) {
        this.orderType = orderType;
    }

    public double getTotalAmountWithoutVAT() {
        double total = 0;
        if (dishOrderList != null) {
            for (DishOrder item : dishOrderList) {
                if (item.getDish() != null && item.getDish().getPrice() != null) {
                    total += item.getDish().getPrice() * item.getQuantity();
                }
            }
        }
        return total;
    }

    public double getTotalAmountWithVAT() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}