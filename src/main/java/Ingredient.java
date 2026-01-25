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

    // 2. CONSTRUCTEURS (Pour créer l'objet facilement)
    public Ingredient() {
    }

    public Ingredient(Integer id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public StockValue getStockValueAt(Instant t) {
        double totalQuantity = 0.0;

        // On parcourt chaque mouvement enregistré dans la liste
        for (StockMovement m : this.stockMovementList) {

            // On vérifie si le mouvement a eu lieu AVANT ou À la date demandée (t)
            if (!m.getCreationDatetime().isAfter(t)) {

                if (m.getType() == MovementType.IN) {
                    // Si c'est une entrée, on ajoute
                    totalQuantity += m.getValue().getQuantity();
                } else if (m.getType() == MovementType.OUT) {
                    // Si c'est une sortie, on soustrait
                    totalQuantity -= m.getValue().getQuantity();
                }
            }
        }
        // On renvoie le résultat final sous forme de StockValue
        return new StockValue(totalQuantity, Unit.KG);
    }
    // 4. GETTERS ET SETTERS (Pour que le DataRetriever puisse remplir l'objet)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public List<StockMovement> getStockMovementList() { return stockMovementList; }

    // C'est ce setter que le DataRetriever utilisera pour donner les mouvements SQL à Java
    public void setStockMovementList(List<StockMovement> mouvements) {
        this.stockMovementList = mouvements;
    }

    // 5. MÉTHODES UTILES (Affichage et Comparaison)
    @Override
    public String toString() {
        return "Ingredient{" + "id=" + id +
                ", name='" + name + '\''
                + ", price=" + price +
                "category" + category +  '}';
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

    public CategoryEnum getCategory() {
        return category;
    }

    // Permet de modifier la catégorie
    public void setCategory(CategoryEnum category) {
        this.category = category;
    }
}






















