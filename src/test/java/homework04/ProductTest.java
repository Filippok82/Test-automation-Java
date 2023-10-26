package homework04;

import org.junit.jupiter.api.*;

import java.sql.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTest {
    private static Connection connection;

    @BeforeAll
    static void init() {
        connect("home_product.db");
        createTable("CREATE TABLE IF NOT EXISTS product\n" +
                "(id integer PRIMARY KEY,\n" +
                "menu_name text NOT NULL,\n" +
                "price integer NOT NULL);");
        createTable("CREATE TABLE orders_products\n" +
                "(order_id INTEGER NOT NULL,\n" +
                " product_id INTEGER NOT NULL,\n" +
                " quantity INTEGER NOT NULL,\n);");
        insertProductInfo(1, "coffee", 500);
        insertProductInfo(2, "tea", 120);
        insertProductInfo(3, "egg", 100);
        insertProductInfo(4, "milk", 80);
        insertProductInfoOrder(1, 1, 10);
        insertProductInfoOrder(2, 2, 5);
        insertProductInfoOrder(3, 3, 8);
    }

    private static void connect(String name) {
        try {
            //Регистрация драйвера
            Class.forName("org.sqlite.JDBC");
            //Создание подключения
            connection = DriverManager.getConnection("jdbc:sqlite:" + name);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Открытие базы данных");
    }

    //Создание произвольной таблицы
    private static void createTable(String sql) {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Добавление в таблицу product
    private static void insertProductInfo(Integer id, String menu_name, Integer price) {
        String sql = "INSERT INTO product(id,menu_name, price) VALUES(?,?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, menu_name);
            pstmt.setInt(3, price);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    //Добавление в таблицу orders_products
    private static void insertProductInfoOrder(Integer order_id, Integer product_id, Integer quantity) {
        String sql = "INSERT INTO orders_products(order_id, product_id, quantity) VALUES(?,?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, order_id);
            pstmt.setInt(2, product_id);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // Проверка созданной таблицы

    @Test
    @Order(1)
    void selectOrdersProducts() {
        String sql = "SELECT * FROM orders_products";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Assertions.assertNotNull(rs);
            int countTableSize = 0;
            while (rs.next()) {
                countTableSize++;
                System.out.println(rs.getInt("order_id") + "\t"
                        + rs.getString("product_id") + "\t" +
                        rs.getInt("quantity"));
            }
            Assertions.assertEquals(3, countTableSize);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    @Order(2)
    void selectProductOneColum() {
        String sql = "SELECT menu_name FROM product";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Assertions.assertEquals("coffee", rs.getString(1));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Удаление строки и проверка
    @Test
    @Order(3)
    void deleteProduct() {
        String sqlDeleteProduct = "DELETE FROM product  WHERE id='1'";


        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sqlDeleteProduct);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlDeleteProduct);
            Assertions.assertNotNull(rs);
            int countTableSize = 0;
            while (rs.next()) {
                countTableSize++;
                System.out.println(rs.getInt("id") + "\t"
                        + rs.getString("menu_name") + "\t" +
                        rs.getInt("price"));
            }
            Assertions.assertEquals(3, countTableSize);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Очищение таблиц после подлючения
    @AfterAll
    static void close() throws SQLException {
        String sqlDeleteProduct = "DELETE FROM product";
        String sqlDeleteOrderProduct = "DELETE FROM orders_products";

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sqlDeleteProduct);
            stmt.execute(sqlDeleteOrderProduct);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
//        Закрытие ресурсов
        connection.close();
        System.out.println("Закрытие базы данных");
    }
}
