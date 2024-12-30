import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private static final String URL = "jdbc:sqlite:products.db";  // Database file

    // Connect to the database
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Initialize the database (Create Products Table)
    public static void initDB() {
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS products (" +
                                    "id TEXT PRIMARY KEY, " +
                                    "name TEXT, " +
                                    "price REAL, " +
                                    "description TEXT, " +
                                    "image TEXT)";
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch all products
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");
            while (rs.next()) {
                Product product = new Product(rs.getString("id"),
                                              rs.getString("name"),
                                              rs.getDouble("price"),
                                              rs.getString("description"),
                                              rs.getString("image"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Insert a new product (for example purposes)
    public static void insertProduct(Product product) {
        try (Connection conn = connect()) {
            String insertSQL = "INSERT INTO products (id, name, price, description, image) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, product.getId());
                pstmt.setString(2, product.getName());
                pstmt.setDouble(3, product.getPrice());
                pstmt.setString(4, product.getDescription());
                pstmt.setString(5, product.getImage());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
