package dao;

import db.DBConnection;
import model.*;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {


    public Order findOrderByReference(String reference) {
        DBConnection dbConnection = new DBConnection();
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
                    SELECT id, reference, creation_datetime FROM "order" WHERE reference = ?""");
            ps.setString(1, reference);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Order order = new Order();
                Integer idOrder = rs.getInt("id");
                order.setId(idOrder);
                order.setReference(rs.getString("reference"));
                order.setCreationDatetime(rs.getTimestamp("creation_datetime").toInstant());
                order.setDishOrderList(findDishOrderByIdOrder(idOrder));
                return order;
            }
            throw new RuntimeException("Order not found with reference " + reference);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DishOrder> findDishOrderByIdOrder(Integer idOrder) {
        DBConnection dbConnection = new DBConnection();
        List<DishOrder> dishOrders = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
                    SELECT id, id_dish, quantity FROM dish_order WHERE id_order = ?""");
            ps.setInt(1, idOrder);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Dish dish = findDishById(rs.getInt("id_dish"));
                DishOrder dishOrder = new DishOrder();
                dishOrder.setId(rs.getInt("id"));
                dishOrder.setQuantity(rs.getInt("quantity"));
                dishOrder.setDish(dish);
                dishOrders.add(dishOrder);
            }
            return dishOrders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveOrder(Order order) {
        // Validation Stock (Logique demandée : Vérifier avant d'enregistrer)
        for (DishOrder line : order.getDishOrderList()) {
            for (DishIngredient recipeLine : line.getDish().getDishIngredients()) {
                Ingredient ing = recipeLine.getIngredient();
                // Utilise la méthode getStockValueAt du prof dans Ingredient.java
                StockValue sv = ing.getStockValueAt(Instant.now());
                double stockActuel = (sv != null) ? sv.getQuantity() : 0.0;
                double requis = recipeLine.getQuantity() * line.getQuantity();

                if (stockActuel < requis) {
                    throw new RuntimeException("Stock insuffisant pour " + ing.getName() + " (Requis: " + requis + ")");
                }
            }
        }

        DBConnection dbConnection = new DBConnection();
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int orderId;
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO \"order\" (reference, creation_datetime) VALUES (?, ?) RETURNING id")) {
                    ps.setString(1, order.getReference());
                    ps.setTimestamp(2, Timestamp.from(order.getCreationDatetime()));
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    orderId = rs.getInt(1);
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO dish_order (id_order, id_dish, quantity) VALUES (?, ?, ?)")) {
                    for (DishOrder line : order.getDishOrderList()) {
                        ps.setInt(1, orderId);
                        ps.setInt(2, line.getDish().getId());
                        ps.setInt(3, line.getQuantity());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish findDishById(Integer id) {
        DBConnection dbConnection = new DBConnection();
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
                    SELECT id, name, dish_type, selling_price FROM dish WHERE id = ?""");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Dish dish = new Dish();
                dish.setId(rs.getInt("id"));
                dish.setName(rs.getString("name"));
                dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type").trim()));
                dish.setPrice(rs.getObject("selling_price") == null ? null : rs.getDouble("selling_price"));
                dish.setDishIngredients(findIngredientByDishId(id));
                return dish;
            }
            throw new RuntimeException("Dish not found " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish saveDish(Dish toSave) {
        DBConnection dbConnection = new DBConnection();
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String upsertDishSql = """
                        INSERT INTO dish (id, selling_price, name, dish_type)
                        VALUES (?, ?, ?, ?::dish_type)
                        ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, 
                        dish_type = EXCLUDED.dish_type, selling_price = EXCLUDED.selling_price
                        RETURNING id""";
                Integer dishId;
                try (PreparedStatement ps = conn.prepareStatement(upsertDishSql)) {
                    ps.setInt(1, toSave.getId() != null ? toSave.getId() : getNextSerialValue(conn, "dish", "id"));
                    ps.setObject(2, toSave.getPrice(), Types.DOUBLE);
                    ps.setString(3, toSave.getName());
                    ps.setString(4, toSave.getDishType().name());
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    dishId = rs.getInt(1);
                }

                // Correction ici pour tes erreurs en rouge
                detachIngredients(conn, toSave.getDishIngredients());
                attachIngredients(conn, toSave.getDishIngredients());

                conn.commit();
                return findDishById(dishId);
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> findIngredientsByCriteria(String name, String category, int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        String sql = "SELECT * FROM ingredient WHERE (? IS NULL OR name ILIKE ?) " +
                "AND (? IS NULL OR category::text = ?) LIMIT ? OFFSET ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, "%" + name + "%");
            ps.setString(3, category);
            ps.setString(4, category);
            ps.setInt(5, size);
            ps.setInt(6, (page - 1) * size);
            try (ResultSet rs = ps.executeQuery()) {
                // Dans findIngredientsByCriteria (DataRetriever.java)
                while (rs.next()) {
                    Ingredient ing = new Ingredient();
                    ing.setId(rs.getInt("id"));
                    ing.setName(rs.getString("name"));
                    ing.setPrice(rs.getDouble("price"));

                    // On convertit le texte de la DB en Enum Java
                    String catStr = rs.getString("category");
                    if (catStr != null) {
                        ing.setCategory(CategoryEnum.valueOf(catStr.trim()));
                    }

                    ingredients.add(ing);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return ingredients;
    }

    public List<StockMovement> findStockMovementsByIngredientId(Integer id) {
        DBConnection dbConnection = new DBConnection();
        List<StockMovement> list = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM stock_movement WHERE id_ingredient = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StockMovement m = new StockMovement();
                m.setId(rs.getInt("id"));
                m.setType(MovementType.valueOf(rs.getString("type").trim())); // Correction MovementTypeEnum
                m.setCreationDatetime(rs.getTimestamp("creation_datetime").toInstant());
                m.setValue(new StockValue(rs.getDouble("quantity"), Unit.valueOf(rs.getString("unit").trim())));
                list.add(m);
            }
            return list;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void saveStockMovement(StockMovement mvt, int idIng) {
        DBConnection dbConnection = new DBConnection();
        try (Connection conn = dbConnection.getConnection()) {
            int nextId = (mvt.getId() != null && mvt.getId() != 0) ? mvt.getId() : getNextSerialValue(conn, "stock_movement", "id");
            PreparedStatement ps = conn.prepareStatement("""
                INSERT INTO stock_movement (id, id_ingredient, quantity, unit, type, creation_datetime)
                VALUES (?, ?, ?, ?::unit_type, ?::movement_type, ?)""");
            ps.setInt(1, nextId);
            ps.setInt(2, idIng);
            ps.setDouble(3, mvt.getValue().getQuantity());
            ps.setString(4, mvt.getValue().getUnit().name());
            ps.setString(5, mvt.getType().name()); // Utilise MovementTypeEnum
            ps.setTimestamp(6, Timestamp.from(mvt.getCreationDatetime()));
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private void detachIngredients(Connection conn, List<DishIngredient> dishIngredients) throws SQLException {
        if (dishIngredients == null || dishIngredients.isEmpty()) return;

        // On récupère l'ID du plat lié au premier DishIngredient
        Integer dishId = dishIngredients.get(0).getDish().getId();

        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM dish_ingredient WHERE id_dish = ?")) {
            ps.setInt(1, dishId);
            ps.executeUpdate();
        }
    }

    private void attachIngredients(Connection conn, List<DishIngredient> ingredients) throws SQLException {
        if (ingredients == null || ingredients.isEmpty()) return;

        String sql = "INSERT INTO dish_ingredient (id, id_ingredient, id_dish, required_quantity, unit) VALUES (?, ?, ?, ?, ?::unit)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (DishIngredient di : ingredients) {
                ps.setInt(1, getNextSerialValue(conn, "dish_ingredient", "id"));
                ps.setInt(2, di.getIngredient().getId());
                ps.setInt(3, di.getDish().getId());
                ps.setDouble(4, di.getQuantity());
                ps.setString(5, di.getUnit().name());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private List<DishIngredient> findIngredientByDishId(Integer idDish) {
        DBConnection dbConnection = new DBConnection();
        List<DishIngredient> dishIngredients = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
                    SELECT i.id, i.name, i.price, i.category, di.required_quantity, di.unit
                    FROM ingredient i JOIN dish_ingredient di ON di.id_ingredient = i.id WHERE di.id_dish = ?""");
            ps.setInt(1, idDish);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ingredient ing = new Ingredient();
                ing.setId(rs.getInt("id"));
                ing.setName(rs.getString("name"));
                ing.setPrice(rs.getDouble("price"));
                ing.setCategory(CategoryEnum.valueOf(rs.getString("category").trim()));

                DishIngredient di = new DishIngredient();
                di.setIngredient(ing);
                di.setQuantity(rs.getDouble("required_quantity"));
                di.setUnit(Unit.valueOf(rs.getString("unit").trim()));
                dishIngredients.add(di);
            }
            return dishIngredients;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private int getNextSerialValue(Connection conn, String tableName, String columnName) throws SQLException {
        String seq = getSerialSequenceName(conn, tableName, columnName);
        updateSequenceNextValue(conn, tableName, columnName, seq);
        try (PreparedStatement ps = conn.prepareStatement("SELECT nextval(?)")) {
            ps.setString(1, seq);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    private String getSerialSequenceName(Connection conn, String tableName, String columnName) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT pg_get_serial_sequence(?, ?)")) {
            ps.setString(1, tableName);
            ps.setString(2, columnName);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : null;
        }
    }

    private void updateSequenceNextValue(Connection conn, String tableName, String columnName, String sequenceName) throws SQLException {
        String sql = String.format("SELECT setval('%s', (SELECT COALESCE(MAX(%s), 0) FROM %s))", sequenceName, columnName, tableName);
        try (Statement stmt = conn.createStatement()) { stmt.executeQuery(sql); }
    }
}