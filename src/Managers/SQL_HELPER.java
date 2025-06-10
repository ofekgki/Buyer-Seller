package Managers;

import Enums.Category;
import Models.*;

import java.sql.*;
import java.util.*;

public class SQL_HELPER {

    private static SQL_HELPER instance;
    private final ManagerBuyer managerBuyer;
    private final ManagerSeller managerSeller;
    private final ManagerProduct managerProduct;

    private final Connection connection = DBConnection.getConnection();

    private SQL_HELPER() {
        managerBuyer = ManagerBuyer.getInstance();
        managerSeller = ManagerSeller.getInstance();
        managerProduct = ManagerProduct.getInstance();

    }

    public static SQL_HELPER getInstance() {
        if (instance == null)
            instance = new SQL_HELPER();
        return instance;
    }

    ///////////////////////////////////////////////LOAD DATABASE//////////////////////////////////
    public void loadDatabase() {
        loadSeller();
        loadBuyer();
        loadProduct();
        loadCart();
        loadCartHistory();

    }

    private void loadCart() {
        try {
            // Establish connection to the database
            Connection conn = DBConnection.getConnection();

            // SQL query to select active shopping carts and their products
            String sql = """
                        SELECT sc.cart_id, sc.buyer_id, p.product_id, p.product_name, p.price, p.category, 
                               sp.package_price
                        FROM ShoppingCart sc
                        JOIN CartProduct ci ON sc.cart_id = ci.cart_id
                        JOIN Product p ON ci.product_id = p.product_id
                        LEFT JOIN SpecialPackage sp ON p.product_id = sp.product_id
                        WHERE sc.status = 'ACTIVE'
                        ORDER BY sc.cart_id
                    """;

            // Prepare and execute the SQL statement
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // A map to group products into carts by buyer ID
            Map<Integer, Cart> carts = new HashMap<>();

            // Iterate over each row in the result set
            while (rs.next()) {
                // Extract buyer and product details from the current row
                int cart_id = rs.getInt("cart_id");
                int buyerId = rs.getInt("buyer_id");
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                double price = rs.getDouble("price");
                String categoryStr = rs.getString("category");

                // If the product has a special package price, retrieve it
                double packagePrice = rs.getObject("package_price") != null ? rs.getDouble("package_price") : 0;
                Category category = Category.valueOf(categoryStr.toUpperCase());

                // Create the correct Product object (normal or special package)
                Product product;
                if (packagePrice > 0) {
                    product = new ProductSpecialPackage(productName, price, category, packagePrice);
                } else {
                    product = new Product(productName, price, category);
                }

                // If this buyer doesn't have a cart yet, create a new one
                if (!carts.containsKey(buyerId)) {
                    carts.put(buyerId, new Cart(cart_id));
                }

                // Add the current product to the buyer's cart
                carts.get(buyerId).addProductToCart(product);
            }

            // After all carts are populated, assign each cart to the corresponding buyer object
            for (Map.Entry<Integer, Cart> entry : carts.entrySet()) {
                int buyerId = entry.getKey();
                Cart cart = entry.getValue();
                Buyer buyer = managerBuyer.getBuyerById(buyerId); // find the buyer object by ID
                if (buyer != null) {
                    buyer.setCurrentCart(cart); // assign the loaded cart to the buyer
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // log SQL errors if any
        }
    }

    private void loadCartHistory() {
        try {

            Connection conn = DBConnection.getConnection();
            String sql = """
                        SELECT sc.cart_id, sc.buyer_id, p.product_id, p.product_name, p.price, p.category, 
                               sp.package_price
                        FROM ShoppingCart sc
                        JOIN CartProduct ci ON sc.cart_id = ci.cart_id
                        JOIN Product p ON ci.product_id = p.product_id
                        LEFT JOIN SpecialPackage sp ON p.product_id = sp.product_id
                        WHERE sc.status = 'COMPLETED'
                        ORDER BY sc.cart_id
                    """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            Map<Integer, Cart> carts = new HashMap<>();

            while (rs.next()) {
                int cart_id = rs.getInt("cart_id");
                int buyerId = rs.getInt("buyer_id");
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                double price = rs.getDouble("price");
                String categoryStr = rs.getString("category");

                double packagePrice = rs.getObject("package_price") != null ? rs.getDouble("package_price") : 0;
                Category category = Category.valueOf(categoryStr.toUpperCase());

                Product product;
                if (packagePrice > 0) {
                    product = new ProductSpecialPackage(productName, price, category, packagePrice);
                } else {
                    product = new Product(productName, price, category);
                }

                if (!carts.containsKey(buyerId)) {
                    carts.put(buyerId, new Cart(cart_id));
                }

                carts.get(buyerId).addProductToCart(product);
            }

            for (Map.Entry<Integer, Cart> entry : carts.entrySet()) {
                int buyerId = entry.getKey();
                Cart cart = entry.getValue();
                Buyer buyer = managerBuyer.getBuyerById(buyerId);
                if (buyer != null) {
                    buyer.getHistoryCart().add(cart);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // log SQL errors if any
        }
    }

    private void loadProduct() {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT p.product_id, p.product_name, p.price, p.category, p.seller_id, sp.package_price " +
                    "FROM Product p LEFT JOIN SpecialPackage sp ON p.product_id = sp.product_id AND p.seller_id = sp.seller_id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("product_name");
                double price = rs.getDouble("price");
                String categoryStr = rs.getString("category");
                int sellerId = rs.getInt("seller_id");
                Category category = Category.valueOf(categoryStr.toUpperCase());
                Double specialPrice = rs.getObject("package_price") != null ? rs.getDouble("package_price") : null;

                Product product;
                if (specialPrice != null) {
                    product = new ProductSpecialPackage(name, price, category, specialPrice);
                } else {
                    product = new Product(name, price, category);
                }

                product.setID(getProductIdByName(name));
                managerProduct.addtolist(product);


                Seller seller = managerSeller.getSellerById(sellerId);
                if (seller != null) {
                    seller.addProduct(product); //לוודא
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSeller() {
        try {
            Statement my_stmt = connection.createStatement();
            ResultSet my_rs;
            my_rs = my_stmt.executeQuery("SELECT * FROM seller");
            while (my_rs.next()) {
                Seller temp = new Seller(my_rs.getInt("seller_id"), my_rs.getString("username"),
                        my_rs.getString("password"));
                managerSeller.addSeller(temp);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBuyer() {
        try {
            Statement my_stmt = connection.createStatement();
            ResultSet my_rs;
            my_rs = my_stmt.executeQuery("SELECT * FROM buyer");
            while (my_rs.next()) {
                Buyer temp = new Buyer(my_rs.getInt("buyer_id"), my_rs.getString("username"),
                        my_rs.getString("password"), new Address(my_rs.getString("street"),
                        my_rs.getString("house_number"), my_rs.getString("city"), my_rs.getString("country")));
                managerBuyer.addBuyer(temp);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getProductIdByName(String productName) {
        try {
            String sql = "SELECT product_id FROM Product WHERE product_name = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, productName);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int productId = rs.getInt("product_id");
                return productId;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }


    ///////////////////////////////////////////////UPDATE DATABASE//////////////////////////////////


    public void addProductToCartInDatabase(Buyer buyer, Product product, Seller seller) {
        try {
            Connection conn = DBConnection.getConnection();

            int buyerId = buyer.getBuyer_id();
            int cartId = -1;

            //Check if the buyer already has an ACTIVE cart
            String checkCartSql = "SELECT cart_id FROM ShoppingCart WHERE buyer_id = ? AND status = 'ACTIVE'";
            PreparedStatement checkCartStmt = conn.prepareStatement(checkCartSql);
            checkCartStmt.setInt(1, buyerId);
            ResultSet cartResult = checkCartStmt.executeQuery();

            if (cartResult.next()) {
                cartId = cartResult.getInt("cart_id");
            } else {
                // Create new cart
                String insertCartSql = "INSERT INTO ShoppingCart (buyer_id, status, cart_date) VALUES (?, 'ACTIVE', CURRENT_DATE) RETURNING cart_id";
                PreparedStatement insertCartStmt = conn.prepareStatement(insertCartSql);
                insertCartStmt.setInt(1, buyerId);
                ResultSet newCartResult = insertCartStmt.executeQuery();
                if (newCartResult.next()) {
                    cartId = newCartResult.getInt("cart_id");
                }
            }

            //Check if the product is already in the cart
            String checkItemSql = "SELECT * FROM CartProduct WHERE cart_id = ? AND buyer_id = ? AND product_id = ? AND seller_id = ?";
            PreparedStatement checkItemStmt = conn.prepareStatement(checkItemSql);
            checkItemStmt.setInt(1, cartId);
            checkItemStmt.setInt(2, buyerId);
            checkItemStmt.setInt(3, product.getProductId());
            checkItemStmt.setInt(4, seller.getSeller_id());
            ResultSet itemResult = checkItemStmt.executeQuery();

            if (itemResult.next()) {
                System.out.println("Product already exists in cart. Skipping insert.");
                return;
            }

            //Insert the product into the cart
            String insertItemSql = "INSERT INTO CartProduct (cart_id, buyer_id, product_id, seller_id) VALUES (?, ?, ?, ?)";
            PreparedStatement insertItemStmt = conn.prepareStatement(insertItemSql);
            insertItemStmt.setInt(1, cartId);
            insertItemStmt.setInt(2, buyerId);
            insertItemStmt.setInt(3, product.getProductId());
            insertItemStmt.setInt(4, seller.getSeller_id());
            insertItemStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public void addNewUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addNewSeller(String username, String password) throws SQLException {
        String sql = "INSERT INTO seller (username, password) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                Seller newSeller = new Seller(generatedId, username, password);
                ManagerSeller.getInstance().addSeller(newSeller);
            }

        }
    }

    public void addNewBuyer(String username, String password, Address address) throws SQLException {
        String sql = "INSERT INTO buyer (username, password, street, house_number, city, country) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, address.getStreet());
            pstmt.setString(4, address.getHouseNum());
            pstmt.setString(5, address.getCity());
            pstmt.setString(6, address.getCountry());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                Buyer newBuyer = new Buyer(generatedId, username, password, address);
                ManagerBuyer.getInstance().addBuyer(newBuyer);
            }
        }
    }

    public static void insertProduct(Product product, int sellerId) {
        String insertProductSQL = "INSERT INTO product (seller_id, product_name, category, price, is_special) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING product_id";
        String insertSpecialSQL = "INSERT INTO specialpackage (product_id, seller_id, package_price) VALUES (?, ?, ?)";


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmtProduct = conn.prepareStatement(insertProductSQL)) {

            pstmtProduct.setInt(1, sellerId);
            pstmtProduct.setString(2, product.getProductName());
            pstmtProduct.setString(3, product.getCategory().name()); // assuming Category is enum
            pstmtProduct.setDouble(4, product.getProductPrice());
            pstmtProduct.setBoolean(5, product instanceof ProductSpecialPackage);

            ResultSet rs = pstmtProduct.executeQuery();
            if (rs.next()) {
                int productId = rs.getInt(1);

                // רק אם זה מוצר עם אריזה מיוחדת
                if (product instanceof ProductSpecialPackage specialProduct) {
                    try (PreparedStatement pstmtSpecial = conn.prepareStatement(insertSpecialSQL)) {
                        pstmtSpecial.setInt(1, productId);
                        pstmtSpecial.setInt(2, sellerId);
                        pstmtSpecial.setDouble(3, specialProduct.getSpecialPackagePrice());
                        pstmtSpecial.executeUpdate();
                    }
                }

                System.out.println("Product inserted successfully with ID: " + productId);
            }

        } catch (SQLException e) {
            System.err.println("Error inserting product: " + e.getMessage());
        }
    }

    public boolean UpdateCart(int buyerIndex) {
        String sql = "UPDATE shoppingcart SET status = 'COMPLETED' WHERE buyer_id = ? AND status = 'ACTIVE'";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, buyerIndex);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void UpdateHistory(int buyerId, int cartId) {
        String deleteSql = "DELETE FROM shoppingcart WHERE buyer_id = ? AND status = 'ACTIVE'";
        String updateSql = "UPDATE shoppingcart SET status = 'ACTIVE' WHERE buyer_id = ? AND cart_id = ?";

        try (Connection connection = DBConnection.getConnection()) {
            // First DELETE active cart
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, buyerId);
                deleteStmt.executeUpdate();
            }

            // Then UPDATE selected history cart to ACTIVE
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                updateStmt.setInt(1, buyerId);
                updateStmt.setInt(2, cartId);
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public int getLastID() {
        String sql = "SELECT MAX(cart_id) FROM shoppingcart";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) + 1; // first column = MAX(cart_id)
            } else {
                return -1; // no rows → no carts yet
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

