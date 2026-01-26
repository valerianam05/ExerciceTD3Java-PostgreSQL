import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

public class Main {
//    public static void main(String[] args) {
//        DataRetriever dataRetriever = new DataRetriever();
//        List<Dish> allDishes = dataRetriever.findAll();
//
//        System.out.println("Pour la méthode getDishCost() :");
//        System.out.println("[\"Plat\", \"Coût attendu\"]");
//
//        for (Dish dish : allDishes) {
//            double cost = dish.getDishCost();
//            System.out.println("[\"" + dish.getName() + "\", \"" + cost + "\"]");
//        }
//
//        System.out.println("\nPour la méthode getGrossMargin() :");
//        System.out.println("[\"Plat\", \"Marge attendue\"]");
//
//        for (Dish dish : allDishes) {
//            String marginOutput;
//            try {
//                // On récupère le chiffre brut
//                double margin = dish.getGrossMargin();
//                marginOutput = String.valueOf(margin);
//            } catch (RuntimeException e) {
//                marginOutput = " Exception (prix NULL)";
//            }
//            System.out.println("[\"" + dish.getName() + "\", \"" + marginOutput + "\"]");
//        }
//    }
//public static void main(String[] args) {
//    DataRetriever dr = new DataRetriever();
//
//    // Création d'une liste pour stocker les données de l'image
//    // Note : On utilise une structure simple pour l'exemple (ID_MOUV, ID_ING, QTY, TYPE, DATE)
//    Object[][] data = {
//            {1, 1, 5.0, MovementType.IN, "2024-01-05T08:00:00Z"},
//            {2, 1, 0.2, MovementType.OUT, "2024-01-06T12:00:00Z"},
//            {3, 2, 4.0, MovementType.IN, "2024-01-05T08:00:00Z"},
//            {4, 2, 0.15, MovementType.OUT, "2024-01-06T12:00:00Z"},
//            {5, 3, 10.0, MovementType.IN, "2024-01-04T09:00:00Z"},
//            {6, 3, 1.0, MovementType.OUT, "2024-01-06T13:00:00Z"},
//            {7, 4, 3.0, MovementType.IN, "2024-01-05T10:00:00Z"},
//            {8, 4, 0.3, MovementType.OUT, "2024-01-06T14:00:00Z"},
//            {9, 5, 2.5, MovementType.IN, "2024-01-05T10:00:00Z"},
//            {10, 5, 0.2, MovementType.OUT, "2024-01-06T14:00:00Z"}
//    };
//
//    for (Object[] row : data) {
//        StockValue value = new StockValue((Double) row[2], Unit.KG);
//        StockMovement m = new StockMovement(
//                (Integer) row[0],
//                value,
//                (MovementType) row[3],
//                Instant.parse((String) row[4])
//        );
//
//        dr.saveStockMovement(m, (Integer) row[1]);
//    }
//
//    System.out.println("Toutes les données de l'image ont été traitées.");
//}


//    public static void main(String[] args) {
//        DataRetriever dr = new DataRetriever();
//        Ingredient fromage = new Ingredient(5, "Fromage", 5000.0);
//        fromage.setCategory(CategoryEnum.ANIMAL);
//
//        // 2. On appelle la méthode de sauvegarde
//        dr.saveIngredient(fromage);
//
//        System.out.println("Test de sauvegarde terminé !");
//    }


//    public static void main(String[] args) {
//        DataRetriever dr = new DataRetriever();
//        List<Ingredient> ingredients = dr.findAllIngredients();
//        System.out.println(ingredients);
//    }
//


//    public static void main(String[] args) {
//        DataRetriever dr = new DataRetriever();
//
//        List<Ingredient> ingredients = dr.findAllIngredients();
//
//        if (ingredients.isEmpty()) {
//            System.out.println("La base est vide. Vérifie tes données SQL !");
//        } else {
//            for (Ingredient ing : ingredients) {
//                System.out.println("\nIngrédient : " + ing.getName().toUpperCase());
//                System.out.println("Prix Unit. : " + ing.getPrice() + " Ar");
//
//                // 2. Calcul du stock en parcourant la liste des mouvements
//                double stockActuel = 0;
//                List<StockMovement> mouvements = ing.getStockMovementList();
//
//                if (mouvements != null && !mouvements.isEmpty()) {
//                    System.out.println("Historique des mouvements :");
//                    for (StockMovement sm : mouvements) {
//                        double qte = sm.getValue().getQuantity();
//                        String unite = sm.getValue().getUnit().toString();
//
//                        if (sm.getType() == MovementType.IN) {
//                            stockActuel += qte;
//                            System.out.println("   Entrée de " + qte + " " + unite);
//                        } else {
//                            stockActuel -= qte;
//                            System.out.println("   Sortie de " + qte + " " + unite);
//                        }
//                    }
//                } else {
//                    System.out.println("  (!) Aucun mouvement enregistré pour cet ingrédient.");
//                }
//
//                System.out.println("STOCK FINAL : " + stockActuel);
//                System.out.println("VALEUR DU STOCK : " + (stockActuel * ing.getPrice()) + " Ar");
//            }
//        }
//    }
//public static void main(String[] args) {
//    DataRetriever dataRetriever = new DataRetriever();
//
//    dataRetriever.showStockStatus();
//}

        public static void main(String[] args) {

                DataRetriever dr = new DataRetriever();
                // On se contente d'afficher l'état actuel calculé depuis la DB
                dr.showStockStatus();
        }
}