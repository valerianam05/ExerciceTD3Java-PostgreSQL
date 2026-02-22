import dao.DataRetriever;
import model.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;




public class Main {

    // try {
//                List<Ingredient> ingredients = retriever.findIngredientsByCriteria("S", null, 1, 5);
//                for (Ingredient ing : ingredients) {
//                    System.out.println(" - " + ing.getName() + " | Prix: " + ing.getPrice() + "€");
//                }
//
//                System.out.println("\n2. Ajout d'un mouvement de stock pour l'ingrédient ID 1...");
//                StockValue val = new StockValue(10.0, Unit.KG);
//                StockMovement mvt = new StockMovement(0, MovementType.IN, Instant.now(), val);
//                retriever.saveStockMovement(mvt, 1);
//                System.out.println(" Mouvement enregistré !");
//
//                System.out.println("\n3. Tentative de création d'une commande...");
//
//                // On récupère un plat existant (ex: ID 1)
//                Dish pizza = retriever.findDishById(1);
//
//                Order maCommande = new Order();
//                maCommande.setReference("CMD-" + System.currentTimeMillis());
//                maCommande.setCreationDatetime(Instant.now());
//
//                DishOrder ligneCommande = new DishOrder();
//                ligneCommande.setDish(pizza);
//                ligneCommande.setQuantity(2);
//
//                List<DishOrder> listeLignes = new ArrayList<>();
//                listeLignes.add(ligneCommande);
//                maCommande.setDishOrderList(listeLignes);
//
//                retriever.saveOrder(maCommande);
//                System.out.println("Commande enregistrée avec succès !");
//
//            } catch (RuntimeException e) {
//                System.err.println(" ERREUR : " + e.getMessage());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
    // Dans ton Main.java
//            System.out.println("--- TEST 1 : AJOUT DE STOCK ---");
//            try {
//                StockValue quantite = new StockValue(50.0, Unit.KG);
//                StockMovement entree = new StockMovement(0, MovementType.IN, Instant.now(), quantite);
//
//                retriever.saveStockMovement(entree, 1);
//                System.out.println(" Stock ajouté avec succès pour l'ingrédient 1");
//            } catch (Exception e) {
//                System.err.println(" Échec de l'ajout de stock : " + e.getMessage());
//            }

//            try {
//                Dish platTropCher = retriever.findDishById(1);
//
//                Order cmdImpossible = new Order();
//                cmdImpossible.setReference("REF-ERROR-" + System.currentTimeMillis());
//                cmdImpossible.setCreationDatetime(Instant.now());
//
//                DishOrder ligne = new DishOrder();
//                ligne.setDish(platTropCher);
//                ligne.setQuantity(10000);
//
//                List<DishOrder> liste = new ArrayList<>();
//                liste.add(ligne);
//                cmdImpossible.setDishOrderList(liste);
//
//                retriever.saveOrder(cmdImpossible);
//                System.out.println(" Erreur : La commande a été acceptée alors que le stock est vide !");
//            } catch (RuntimeException e) {
//                System.out.println(" Test réussi ! Le système a bien bloqué la commande : " + e.getMessage());
//            }
//            try {
//                Order nouvelleCommande = new Order();
//                nouvelleCommande.setReference(maRef);
//                nouvelleCommande.setCreationDatetime(Instant.now());
//                nouvelleCommande.setType("EAT_IN");
//                nouvelleCommande.setStatus("CREATED");
//
//                System.out.println("\n1. Enregistrement initial...");
//                dao.saveOrder(nouvelleCommande);
//
//                nouvelleCommande.setStatus("READY");
//                nouvelleCommande.setType("TAKE_AWAY");
//                dao.saveOrder(nouvelleCommande);
//
//                nouvelleCommande.setStatus("DELIVERED");
//                dao.saveOrder(nouvelleCommande);
//
//                System.out.println("\n4. Tentative de modification d'une commande LIVRÉE (Interdit)...");
//                nouvelleCommande.setType("EAT_IN");
//
//                dao.saveOrder(nouvelleCommande);
//
//            } catch (RuntimeException e) {
//                System.out.println(" RÉSULTAT ATTENDU : Blocage réussi !");
//                System.out.println("Message d'erreur : " + e.getMessage());
//            } catch (Exception e) {
//                System.err.println(" Erreur inattendue : " + e.getMessage());
//                e.printStackTrace();
//            }

//    public static void main(String[] args) {
//        DataRetriever data = new DataRetriever();
//        Order order = data.findOrderByReference("ORD100");
//        order.setReference(order.getType());
//        data.saveOrder(order);
//
//        }
//
//    public static void main(String[] args) {
//        DataRetriever dr = new DataRetriever();

//                DataRetriever retriever = new DataRetriever();
//                int idCible = 2;
//                Instant maintenant = Instant.now();
//
//                StockValue stockSQL = retriever.getStockValueAt(maintenant, idCible);
//
//                System.out.println("Approche SQL (Push-down) : " + stockSQL.getQuantity() + " " + stockSQL.getUnit());
//
//                Ingredient ingredient = retriever.getIngredientWithMovements(idCible);
//
//                if (ingredient != null) {
//                    StockValue stockJava = ingredient.getStockValueAt(maintenant);
//                    System.out.println("Approche Objet (Java)    : " + stockJava.getQuantity() + " " + stockJava.getUnit());
//
//                    if (stockSQL.getQuantity() == stockJava.getQuantity()) {
//                        System.out.println(" Les deux approches donnent le même résultat !");
//                    } else {
//                        System.out.println("
//                        Erreur : Les chiffres sont différents.");
//                    }
//                }
//            }

//
//        Integer idPlat = 1;
//
//
//        double coutPlat = dr.getDishCost(idPlat);
//        System.out.println("Coût de revient du plat (ID " + idPlat + ") : " + coutPlat + " Ar");
//
//        Double margeBrute = dr.getGrossMargin(idPlat);
//        System.out.println("Marge brute du plat (ID " + idPlat + ")     : " + margeBrute + " Ar");
//
//        System.out.println("Prix de vente estimé (Coût + Marge) : " + (coutPlat + margeBrute) + " Ar");


}





