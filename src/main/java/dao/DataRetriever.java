package dao;

import db.DBConnection;
import model.*;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    // Helper pour centraliser la connexion
    private Connection getConnection() throws SQLException {
        return new DBConnection().getConnection();
    }

    public List<Dish> findAll() {
        List<Dish> allDishes = new ArrayList<>();
        String sql = "SELECT id FROM dish";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Dish d = this.findDishById(rs.getInt("id"));
                if (d != null) allDishes.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allDishes;
    }

    public Dish findDishById(Integer id) {
        String sql = "SELECT id, name, selling_price FROM dish WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Dish dish = new Dish();
                    dish.setId(rs.getInt("id"));
                    dish.setName(rs.getString("name"));
                    dish.setPrice(rs.getObject("selling_price") != null ? rs.getDouble("selling_price") : null);
                    dish.setRecipe(this.findRecipeByDish(id));
                    return dish;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<DishIngredient> findRecipeByDish(Integer idDish) {
        List<DishIngredient> recipe = new ArrayList<>();
        // On évite le SELECT * pour être précis
        String sql = "SELECT i.id, i.name, i.price, i.category, di.required_quantity, di.unit " +
                "FROM ingredient i " +
                "JOIN dish_ingredient di ON i.id = di.id_ingredient WHERE di.id_dish = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDish);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ingredient ing = new Ingredient();
                    ing.setId(rs.getInt("id"));
                    ing.setName(rs.getString("name"));
                    ing.setPrice(rs.getDouble("price"));
                    ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));

                    double qty = rs.getDouble("required_quantity");
                    Unit unit = Unit.valueOf(rs.getString("unit").toUpperCase());

                    recipe.add(new DishIngredient(ing, qty, unit));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    public void saveDish(Dish dish) {
        String updateDish = "UPDATE dish SET name = ?, selling_price = ? WHERE id = ?";
        String deleteLinks = "DELETE FROM dish_ingredient WHERE id_dish = ?";
        String insertLink = "INSERT INTO dish_ingredient (id_dish, id_ingredient, required_quantity, unit) VALUES (?, ?, ?, ?::unit_enum)";

        try (Connection conn = getConnection()) {
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
        String sql = "INSERT INTO stock_movement (id, id_ingredient, quantity, unit, type, creation_datetime) " +
                "VALUES (?, ?, ?, ?::unit_type, ?::movement_type, ?) " +
                "ON CONFLICT (id) DO NOTHING";

        try (Connection conn = getConnection();
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
        // CHANGEMENT : ON CONFLICT DO NOTHING (Consigne implicite) + Cast Enum si nécessaire
        String sql = "INSERT INTO ingredient (id, name, price, category) " +
                "VALUES (?, ?, ?, ?::ingredient_category) " +
                "ON CONFLICT (id) DO NOTHING";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, toSave.getId());
            ps.setString(2, toSave.getName());
            ps.setDouble(3, toSave.getPrice());
            ps.setString(4, toSave.getCategory().toString());

            ps.executeUpdate();

            if (toSave.getStockMovementList() != null) {
                for (StockMovement movement : toSave.getStockMovementList()) {
                    this.saveStockMovement(movement, toSave.getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toSave;
    }

    public List<Ingredient> findAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT id, name, price, category FROM ingredient";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ingredient ing = new Ingredient();
                ing.setId(rs.getInt("id"));
                ing.setName(rs.getString("name"));
                ing.setPrice(rs.getDouble("price"));

                String catStr = rs.getString("category");
                if (catStr != null) ing.setCategory(CategoryEnum.valueOf(catStr));

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
        String sql = "SELECT id, quantity, unit, type, creation_datetime FROM stock_movement WHERE id_ingredient = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idIngredient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StockValue sv = new StockValue(rs.getDouble("quantity"), Unit.valueOf(rs.getString("unit")));
                    StockMovement sm = new StockMovement(
                            rs.getInt("id"),
                            sv,
                            MovementType.valueOf(rs.getString("type")),
                            rs.getTimestamp("creation_datetime").toInstant()
                    );
                    movements.add(sm);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movements;
    }

    public void displayAllMovements() {
        String sql = "SELECT id, id_ingredient, quantity, type, unit, creation_datetime " +
                "FROM stock_movement ORDER BY creation_datetime ASC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("ID | Ingred_ID | Quantité | Type | Unité | Date");
            while (rs.next()) {
                System.out.printf("%d  | %d         | %.2f     | %s  | %s   | %s%n",
                        rs.getInt("id"), rs.getInt("id_ingredient"), rs.getDouble("quantity"),
                        rs.getString("type"), rs.getString("unit"), rs.getTimestamp("creation_datetime"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}