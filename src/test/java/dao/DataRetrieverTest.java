//package dao;
//
//import model.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.Instant;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class DataRetrieverTest {
//    private DataRetriever dr;
//
//    @BeforeEach
//    void setUp() {
//        dr = new DataRetriever();
//    }
//
//    // 1. TEST : getStockValueAt (Logique temporelle - Unitaire)
//    @Test
//    void testGetStockValueAt_TemporalLogic() {
//        Ingredient ing = new Ingredient(99, "Test Ing", 1.0, CategoryEnum.OTHER);
//        Instant t1 = Instant.parse("2024-01-01T10:00:00Z");
//        Instant t2 = Instant.parse("2024-01-02T10:00:00Z"); // Date du calcul
//        Instant t3 = Instant.parse("2024-01-03T10:00:00Z");
//
//        // Mouvement 1 : +10 le 01 Janvier
//        StockMovement m1 = new StockMovement(901, new StockValue(10.0, Unit.KG), MovementType.IN, t1);
//        // Mouvement 2 : -3 le 03 Janvier
//        StockMovement m2 = new StockMovement(902, new StockValue(3.0, Unit.KG), MovementType.OUT, t3);
//
//        ing.setStockMovementList(List.of(m1, m2));
//
//        // ACT : On calcule le stock au 02 Janvier
//        double stockAuT2 = ing.getStockValueAt(t2).getQuantity();
//
//        // ASSERT : On doit avoir 10 (le -3 n'est pas encore arrivé)
//        assertEquals(10.0, stockAuT2, "Le stock au T2 ne devrait compter que le premier mouvement");
//    }
//
//    // 2. TEST : saveStockMovement (Idempotence / Doublon)
//    @Test
//    void testSaveStockMovement_Idempotency() {
//        Instant now = Instant.now();
//        StockMovement m = new StockMovement(888, new StockValue(5.0, Unit.KG), MovementType.IN, now);
//
//        // On insère deux fois le même ID
//        assertDoesNotThrow(() -> {
//            dr.saveStockMovement(m, 1); // 1er essai
//            dr.saveStockMovement(m, 1); // 2ème essai (doublon d'ID)
//        }, "Le ON CONFLICT DO NOTHING devrait empêcher une exception SQL");
//    }
//
//    // 3. TEST : saveIngredient (Cascade / Persistance)
//    @Test
//    void testSaveIngredient_WithMovements() {
//        Ingredient ing = new Ingredient(77, "Cascade Test", 2.0, CategoryEnum.ANIMAL);
//        StockMovement m = new StockMovement(771, new StockValue(1.0, Unit.KG), MovementType.IN, Instant.now());
//        ing.setStockMovementList(List.of(m));
//
//        // Sauvegarde
//        dr.saveIngredient(ing);
//
//        // Vérification : on recharge l'ingrédient pour voir si le mouvement est là
//        List<Ingredient> list = dr.findAllIngredients();
//        Ingredient saved = list.stream().filter(i -> i.getId() == 77).findFirst().orElse(null);
//
//        assertNotNull(saved);
//        assertFalse(saved.getStockMovementList().isEmpty(), "Le mouvement aurait dû être sauvegardé en cascade");
//    }
//
//    // 4. TEST : findRecipeByDish (Jointure)
//    @Test
//    void testFindRecipeByDish_Mapping() {
//        // On suppose que le plat ID 1 existe en base (ex: Burger)
//        Dish dish = dr.findDishById(1);
//
//        if (dish != null && !dish.getRecipe().isEmpty()) {
//            DishIngredient firstLine = dish.getRecipe().get(0);
//
//            assertNotNull(firstLine.getIngredient().getName(), "L'ingrédient dans la recette doit avoir un nom (jointure)");
//            assertTrue(firstLine.getQuantity() > 0, "La quantité requise doit être positive");
//        }
//    }
//
//}