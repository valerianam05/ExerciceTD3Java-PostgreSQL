import dao.DataRetriever;
import model.*;

import java.util.List;
import java.time.Instant;

public class Main {
    /* // TEST DES COUTS ET MARGES DES PLATS
    public static void main(String[] args) {
        dao.DataRetriever dataRetriever = new dao.DataRetriever();
        List<model.Dish> allDishes = dataRetriever.findAll();

        System.out.println("Pour la méthode getDishCost() :");
        System.out.println("[\"Plat\", \"Coût attendu\"]");

        for (model.Dish dish : allDishes) {
            double cost = dish.getDishCost();
            System.out.println("[\"" + dish.getName() + "\", \"" + cost + "\"]");
        }

        System.out.println("\nPour la méthode getGrossMargin() :");
        System.out.println("[\"Plat\", \"Marge attendue\"]");

        for (model.Dish dish : allDishes) {
            String marginOutput;
            try {
                // On récupère le chiffre brut
                double margin = dish.getGrossMargin();
                marginOutput = String.valueOf(margin);
            } catch (RuntimeException e) {
                marginOutput = " Exception (prix NULL)";
            }
            System.out.println("[\"" + dish.getName() + "\", \"" + marginOutput + "\"]");
        }
    }
    */

    /*
    // INSERTION INITIALE DES DONNÉES DE L'IMAGE DU PROF
    public static void main(String[] args) {
        dao.DataRetriever dr = new dao.DataRetriever();

        // Création d'une liste pour stocker les données de l'image
        Object[][] data = {
                {1, 1, 5.0, model.MovementType.IN, "2024-01-05T08:00:00Z"},
                {2, 1, 0.2, model.MovementType.OUT, "2024-01-06T12:00:00Z"},
                {3, 2, 4.0, model.MovementType.IN, "2024-01-05T08:00:00Z"},
                {4, 2, 0.15, model.MovementType.OUT, "2024-01-06T12:00:00Z"},
                {5, 3, 10.0, model.MovementType.IN, "2024-01-04T09:00:00Z"},
                {6, 3, 1.0, model.MovementType.OUT, "2024-01-06T13:00:00Z"},
                {7, 4, 3.0, model.MovementType.IN, "2024-01-05T10:00:00Z"},
                {8, 4, 0.3, model.MovementType.OUT, "2024-01-06T14:00:00Z"},
                {9, 5, 2.5, model.MovementType.IN, "2024-01-05T10:00:00Z"},
                {10, 5, 0.2, model.MovementType.OUT, "2024-01-06T14:00:00Z"}
        };

        for (Object[] row : data) {
            model.StockValue value = new model.StockValue((Double) row[2], model.Unit.KG);
            model.StockMovement m = new model.StockMovement(
                    (Integer) row[0],
                    value,
                    (model.MovementType) row[3],
                    Instant.parse((String) row[4])
            );

            dr.saveStockMovement(m, (Integer) row[1]);
        }

        System.out.println("Toutes les données de l'image ont été traitées.");
    }
    */

    /*
    // TEST SAUVEGARDE UN INGRÉDIENT
    public static void main(String[] args) {
        dao.DataRetriever dr = new dao.DataRetriever();
        model.Ingredient fromage = new model.Ingredient(5, "Fromage", 5000.0, model.CategoryEnum.ANIMAL);

        // 2. On appelle la méthode de sauvegarde
        dr.saveIngredient(fromage);

        System.out.println("Test de sauvegarde terminé !");
    }
    */

    /*
    // TEST RÉCUPÉRATION SIMPLE
    public static void main(String[] args) {
        dao.DataRetriever dr = new dao.DataRetriever();
        List<model.Ingredient> ingredients = dr.findAllIngredients();
        System.out.println(ingredients);
    }
    */

    /*
    // TEST HISTORIQUE DÉTAILLÉ ET CALCUL MANUEL
    public static void main(String[] args) {
        dao.DataRetriever dr = new dao.DataRetriever();

        List<model.Ingredient> ingredients = dr.findAllIngredients();

        if (ingredients.isEmpty()) {
            System.out.println("La base est vide. Vérifie tes données SQL !");
        } else {
            for (model.Ingredient ing : ingredients) {
                System.out.println("\nIngrédient : " + ing.getName().toUpperCase());
                System.out.println("Prix : " + ing.getPrice() + " Ar");

                double stockActuel = 0;
                List<model.StockMovement> mouvements = ing.getStockMovementList();

                if (mouvements != null && !mouvements.isEmpty()) {
                    System.out.println("Historique des mouvements :");
                    for (model.StockMovement sm : mouvements) {
                        double qte = sm.getValue().getQuantity();
                        String unite = sm.getValue().getUnit().toString();

                        if (sm.getType() == model.MovementType.IN) {
                            stockActuel += qte;
                            System.out.println("   Entrée de " + qte + " " + unite);
                        } else {
                            stockActuel -= qte;
                            System.out.println("   Sortie de " + qte + " " + unite);
                        }
                    }
                } else {
                    System.out.println("  (!) Aucun mouvement enregistré pour cet ingrédient.");
                }

                System.out.println("STOCK FINAL : " + stockActuel);
                System.out.println("VALEUR DU STOCK : " + (stockActuel * ing.getPrice()) + " Ar");
            }
        }
    }
    */

    public static void main(String[] args) {
        DataRetriever dr = new DataRetriever();

        System.out.println("TEST TABLEAU 01 : LISTE DES MOUVEMENTS ENREGISTRÉS");
        System.out.println("--------------------------------------------------");
        dr.displayAllMovements();
        System.out.println();


        // Date T du sujet pour la vérification
        Instant dateT = Instant.parse("2025-01-06T23:59:59Z");

        // On récupère les objets Ingredients chargés depuis la DB
        List<Ingredient> ingredients = dr.findAllIngredients();

        System.out.println("TEST TABLEAU 02 : ÉTAT DES STOCKS AU " + dateT);
        System.out.println("--------------------------------------------------");

        for (Ingredient ing : ingredients) {
            // Appel de la logique métier dans Ingredient.java
            StockValue valueAtT = ing.getStockValueAt(dateT);
            double stockCalcule = valueAtT.getQuantity();
            String unite = valueAtT.getUnit().toString();

            System.out.printf("Ingrédient : %-15s | Stock à date T : %6.2f %s%n",
                    ing.getName(), stockCalcule, unite);
        }
    }
}