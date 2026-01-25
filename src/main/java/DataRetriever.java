import db.DBConnection;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    public List<Dish> findAll() {
        List<Dish> allDishes = new ArrayList<>();
        String sql = "SELECT id FROM dish"; // On récupère d'abord tous les IDs

        try (Connection conn = new DBConnection().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // On appelle findDishById pour chaque ID trouvé
                // Cela garantit que chaque plat a sa liste de DishIngredient chargée
                Dish d = this.findDishById(rs.getInt("id"));
                if (d != null) {
                    allDishes.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allDishes;
    }

    public Dish findDishById(Integer id) {
        // On remplace "SELECT *" par les colonnes explicites
        String sql = "SELECT id, name, selling_price FROM dish WHERE id = ?";

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Dish dish = new Dish();
                dish.setId(rs.getInt("id"));
                dish.setName(rs.getString("name"));
                // Utilisation de getObject pour gérer le cas où le prix est NULL en base
                dish.setPrice(rs.getObject("selling_price") != null ? rs.getDouble("selling_price") : null);

                // Charge la recette complète
                dish.setRecipe(this.findRecipeByDish(id));
                return dish;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<DishIngredient> findRecipeByDish(Integer idDish) {
        List<DishIngredient> recipe = new ArrayList<>();
        String sql = "SELECT i.*, di.required_quantity, di.unit FROM ingredient i " +
                "JOIN dish_ingredient di ON i.id = di.id_ingredient WHERE di.id_dish = ?";

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDish);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ingredient ing = new Ingredient();
                ing.setId(rs.getInt("id"));
                ing.setName(rs.getString("name"));
                ing.setPrice(rs.getDouble("price"));

                double qty = rs.getDouble("required_quantity");
                Unit unit = Unit.valueOf(rs.getString("unit").toUpperCase());

                recipe.add(new DishIngredient(ing, qty, unit));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    public void saveDish(Dish dish) {
        String updateDish = "UPDATE dish SET name = ?, selling_price = ? WHERE id = ?";
        String deleteLinks = "DELETE FROM dish_ingredient WHERE id_dish = ?";
        // on fait delete parce que
        //
        // Note le ::unit_enum pour éviter l'erreur de type sur PostgreSQL
        String insertLink = "INSERT INTO dish_ingredient (id_dish, id_ingredient, required_quantity, unit) VALUES (?, ?, ?, ?::unit_enum)";

        try (Connection conn = new DBConnection().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(updateDish);
                 PreparedStatement ps2 = conn.prepareStatement(deleteLinks);
                 PreparedStatement ps3 = conn.prepareStatement(insertLink)) {

                ps1.setString(1, dish.getName());
                if (dish.getPrice() == null) ps1.setNull(2, Types.DOUBLE);
                else ps1.setDouble(2, dish.getPrice());
                ps1.setInt(3, dish.getId());
                ps1.executeUpdate();

                ps2.setInt(1, dish.getId());
                ps2.executeUpdate();

                for (DishIngredient di : dish.getRecipe()) {
                    ps3.setInt(1, dish.getId());
                    ps3.setInt(2, di.getIngredient().getId());
                    ps3.setDouble(3, di.getQuantity());
                    ps3.setString(4, di.getUnit().name());
                    ps3.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveStockMovement(StockMovement movement, int idIngredient) {
        // LE CHANGEMENT EST ICI : on ajoute les types pour PostgreSQL
        String sql = "INSERT INTO stock_movement (id, id_ingredient, quantity, unit, type, creation_datetime) " +
                "VALUES (?, ?, ?, ?::unit_type, ?::movement_type, ?) " +
                "ON CONFLICT (id) DO NOTHING";

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movement.getId());
            ps.setInt(2, idIngredient);
            ps.setDouble(3, movement.getValue().getQuantity());
            ps.setString(4, movement.getValue().getUnit().toString());
            ps.setString(5, movement.getType().toString());
            ps.setTimestamp(6, Timestamp.from(movement.getCreationDatetime()));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Ingredient saveIngredient(Ingredient toSave) {
        // ON ENLÈVE le "::category_enum" car ta colonne est un varchar
        String sql = "INSERT INTO ingredient (id, name, price, category) " +
                "VALUES (?, ?, ?, ?) " + // Juste 4 points d'interrogation
                "ON CONFLICT (id) DO UPDATE SET " +
                "name = EXCLUDED.name, price = EXCLUDED.price, category = EXCLUDED.category";

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, toSave.getId());
            ps.setString(2, toSave.getName());
            ps.setDouble(3, toSave.getPrice());

            // Ici, category est envoyé comme un texte simple, ce que PostgreSQL attend (varchar)
            ps.setString(4, toSave.getCategory().toString());

            ps.executeUpdate();
            if (toSave.getStockMovementList() != null) {
                for (StockMovement movement : toSave.getStockMovementList()) {
                    // On utilise la méthode que tu as déjà écrite pour les mouvements
                    this.saveStockMovement(movement, toSave.getId());
                }
            }

            System.out.println("Ingrédient et ses mouvements sauvegardés !");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toSave;
    }

    public List<Ingredient> findAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();

        // On liste chaque colonne séparément
        String sql = "SELECT id, name, price, category FROM ingredient";

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ingredient ing = new Ingredient();
                // On récupère les données par le nom des colonnes listées plus haut
                ing.setId(rs.getInt("id"));
                ing.setName(rs.getString("name"));
                ing.setPrice(rs.getDouble("price"));

                String catStr = rs.getString("category");
                if (catStr != null) {
                    ing.setCategory(CategoryEnum.valueOf(catStr));
                }

                // On n'oublie pas de charger les mouvements (Question 2.c)
                ing.setStockMovementList(this.getStockMovements(ing.getId()));

                ingredients.add(ing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    public List<StockMovement> getStockMovements(int idIngredient) {
        List<StockMovement> movements = new ArrayList<>();
        // Au lieu de "SELECT * FROM stock_movement..."
        String sql = "SELECT id, quantity, unit, type, creation_datetime FROM stock_movement WHERE id_ingredient = ?";

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idIngredient);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                double quantity = rs.getDouble("quantity");

                // On récupère le String de la base de données
                String unitStr = rs.getString("unit");

                // CONVERSION : On transforme le String en objet Unit
                Unit unitEnum = Unit.valueOf(unitStr);

                MovementType type = MovementType.valueOf(rs.getString("type"));
                Instant date = rs.getTimestamp("creation_datetime").toInstant();

                // Maintenant, les types correspondent ! (double, Unit)
                StockValue stockValue = new StockValue(quantity, unitEnum);

                StockMovement sm = new StockMovement(id, stockValue, type, date);
                movements.add(sm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movements;
    }

}