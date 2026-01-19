import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    public Dish findDishById(Integer id) {
        try (Connection conn = new DBConnection().getConnection()) {
            String sql = "SELECT * FROM dish WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Dish dish = new Dish();
                dish.setId(rs.getInt("id"));
                dish.setName(rs.getString("name"));
                dish.setPrice(rs.getObject("selling_price") != null ? rs.getDouble("selling_price") : null);

                // On charge les ingrédients via la jointure
                dish.setIngredients(this.findIngredientsByDish(id));
                return dish;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    private List<Ingredient> findIngredientsByDish(Integer idDish) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT i.*, di.required_quantity FROM ingredient i " +
                "JOIN dish_ingredient di ON i.id = di.id_ingredient WHERE di.id_dish = ?";
        try (Connection conn = new DBConnection().getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idDish);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ingredient ing = new Ingredient();
                ing.setId(rs.getInt("id"));
                ing.setName(rs.getString("name"));
                ing.setPrice(rs.getDouble("price"));
                ing.setQuantity(rs.getDouble("required_quantity")); // Quantité de la table de jointure
                ingredients.add(ing);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ingredients;
    }

    public void saveDish(Dish dish) {
        // Sauvegarde de la nouvelle entité (Plat + Liens)
        String updateDish = "UPDATE dish SET name = ?, selling_price = ? WHERE id = ?";
        String deleteLinks = "DELETE FROM dish_ingredient WHERE id_dish = ?";
        String insertLink = "INSERT INTO dish_ingredient (id_dish, id_ingredient, required_quantity, unit) VALUES (?, ?, ?, 'KG')";

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

                for (Ingredient ing : dish.getIngredients()) {
                    ps3.setInt(1, dish.getId());
                    ps3.setInt(2, ing.getId());
                    ps3.setDouble(3, ing.getQuantity());
                    ps3.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) { conn.rollback(); throw e; }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}