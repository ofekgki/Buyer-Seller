import Enums.Category;
import Managers.*;
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


    public void loadDatabase() {
        loadSeller();
        loadBuyer();
        loadProduct();
    }

    private void loadProduct() {
        try {

            Connection conn = DBConnection.getConnection();

            String sql = "SELECT p.product_id, p.product_name, p.price, p.category, sp.package_price " +
                    "FROM Product p LEFT JOIN SpecialPackage sp ON p.product_id = sp.product_id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("product_name");
                double price = rs.getDouble("price");
                String categoryStr = rs.getString("category");
                Category category = Category.valueOf(categoryStr.toUpperCase());
                Double specialPrice = rs.getObject("package_price") != null ? rs.getDouble("package_price") : null;

                Product product;
                if (specialPrice != null) {
                    product = new ProductSpecialPackage(id, name, price, category, specialPrice);
                } else {
                    product = new Product(id, name, price, category);
                }

                managerProduct.addtolist(product);
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



}
