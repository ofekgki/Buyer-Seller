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
    //private final ManagerFacade managerFacade;
    private final Connection connection = DBConnection.getConnection();
    private SQL_HELPER() {
        managerBuyer = ManagerBuyer.getInstance();
        managerSeller = ManagerSeller.getInstance();
        managerProduct = ManagerProduct.getInstance();
        //managerFacade = ManagerFacade.getInstance();
    }

    public static SQL_HELPER getInstance() {
        if (instance == null)
            instance = new SQL_HELPER();
        return instance;
    }


    public void loadDatabase() {
        loadSeller();
        loadBuyer();
        loadProduct();
        loadCart();

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
            JOIN CartItem ci ON sc.cart_id = ci.cart_id
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
                    product = new ProductSpecialPackage(productId, productName, price, category, packagePrice);
                } else {
                    product = new Product(productId, productName, price, category);
                }

                // If this buyer doesn't have a cart yet, create a new one
                if (!carts.containsKey(buyerId)) {
                    carts.put(buyerId, new Cart());
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
                    product = new ProductSpecialPackage(id, name, price, category, specialPrice);
                } else {
                    product = new Product(id, name, price, category);
                }

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
                Seller temp = new Seller(my_rs.getInt("seller_id"),my_rs.getString("username"),
                        my_rs.getString("password"));
                managerSeller.addSeller(temp);

            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadBuyer() {
        try {
            Statement my_stmt = connection.createStatement();
            ResultSet my_rs;
            my_rs = my_stmt.executeQuery("SELECT * FROM buyer");
            while (my_rs.next()) {
                Buyer temp = new Buyer(my_rs.getInt("buyer_id"),my_rs.getString("username"),
                        my_rs.getString("password"),new Address(my_rs.getString("street"),
                        my_rs.getString("house_number"),my_rs.getString("city"),my_rs.getString("country")));
                managerBuyer.addBuyer(temp);

            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }




    ///////////////////////////////////////////////UPDATE DATABASE//////////////////////////////////



    public void addProductToCartInDatabase(Buyer buyer, Product product,Seller seller) {
        try {
            Connection conn = DBConnection.getConnection();

            int buyerId = buyer.getBuyer_id();
            int cartId = -1;

            // Step 1: Check if the buyer already has an ACTIVE cart
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

            // Step 2: Check if the product is already in the cart
            String checkItemSql = "SELECT * FROM CartItem WHERE cart_id = ? AND buyer_id = ? AND product_id = ? AND seller_id = ?";
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

            // Step 3: Insert the product into the cart
            String insertItemSql = "INSERT INTO CartItem (cart_id, buyer_id, product_id, seller_id) VALUES (?, ?, ?, ?)";
            PreparedStatement insertItemStmt = conn.prepareStatement(insertItemSql);
            insertItemStmt.setInt(1, cartId);
            insertItemStmt.setInt(2, buyerId);
            insertItemStmt.setInt(3, product.getProductId());
            insertItemStmt.setInt(4, seller.getSeller_id());
            insertItemStmt.executeUpdate();

            System.out.println("Product added to cart (cart_id = " + cartId + ").");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
