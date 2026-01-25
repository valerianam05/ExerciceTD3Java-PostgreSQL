import java.time.Instant;
public class StockMovement {
    private int id;
    private StockValue value; // Utilise la classe StockValue ci-dessus
    private MovementType type; // Utilise l'Enum IN/OUT
    private Instant creationDatetime; // La date précise

    public StockMovement(int id, StockValue value, MovementType type, Instant date) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.creationDatetime = date;
    }


    public StockValue getValue() { return value; }
    public MovementType getType() { return type; }
    public Instant getCreationDatetime() { return creationDatetime; }

    public int getId() {
        return id;
    }
}
