package model;

import java.time.Instant;
import java.util.Objects;

public class StockMovement {
    private Integer id;
    private MovementType type; // Correction : Utilisation du nom exact MovementTypeEnum
    private Instant creationDatetime;
    private StockValue value;

    // 1. Constructeur vide (Indispensable pour le DataRetriever)
    public StockMovement() {
    }

    // 2. Constructeur avec paramètres (Utile pour créer des mouvements manuellement)
    public StockMovement(Integer id, MovementType type, Instant creationDatetime, StockValue value) {
        this.id = id;
        this.type = type;
        this.creationDatetime = creationDatetime;
        this.value = value;
    }

    // 3. Getters et Setters (Tous doivent être présents pour que JDBC fonctionne)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public StockValue getValue() {
        return value;
    }

    public void setValue(StockValue value) {
        this.value = value;
    }

    // 4. Equals et HashCode (Style professeur)
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StockMovement that)) return false;
        return Objects.equals(id, that.id) &&
                type == that.type &&
                Objects.equals(creationDatetime, that.creationDatetime) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, creationDatetime, value);
    }

    // 5. ToString pour faciliter le débuggage dans la console
    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", type=" + type +
                ", creationDatetime=" + creationDatetime +
                ", value=" + value +
                '}';
    }
}