public class Main {
    public static void main(String[] args) {
        DataRetriever dr = new DataRetriever();

//        System.out.println("[\"Plat\", \"Coût attendu\", \"Marge attendue\"]");
//
//        for (int i = 1; i <= 5; i++) {
//            Dish d = dr.findDishById(i);
//            if (d != null) {
//                String coutStr = String.format("%.2f", d.getDishCost());
//                String margeStr;
//                try {
//                    margeStr = String.format("%.2f", d.getGrossMargin());
//                } catch (RuntimeException e) {
//                    margeStr = "EXCEPTION (Prix NULL)";
//                }
//                // Affichage formaté en tableau comme demandé
//                System.out.println("[\"" + d.getName() + "\", \"" + coutStr + "\", \"" + margeStr + "\"]");
//            }
//        }

//
//        // 1. On récupère un plat existant (Récupération)
//        Dish salade = dr.findDishById(1);
//        System.out.println("AVANT SAUVEGARDE : [\"" + salade.getId() + "\", \"" + salade.getName() + "\", \"" + salade.getPrice() + "\"]");
//
//        // 2. On modifie ses données (Mise à jour)
//        salade.setName("Salade fraîche Premium");
//        salade.setPrice(5000.0);
//
//        // 3. On utilise la méthode saveDish pour sauvegarder (Sauvegarde)
//        dr.saveDish(salade);

//        // 4. On affiche toute la table Dish pour prouver que la mise à jour a fonctionné
//        System.out.println("\nAPRÈS SAUVEGARDE (Contenu de la table Dish) :");
//        System.out.println("[\"id\", \"name\", \"selling_price\"]");
//        for (int i = 1; i <= 5; i++) {
//            Dish d = dr.findDishById(i);
//            if (d != null) {
//                System.out.println("[\"" + d.getId() + "\", \"" + d.getName() + "\", \"" + d.getPrice() + "\"]");
//            }
//        }
    }
}