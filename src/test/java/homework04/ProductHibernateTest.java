package homework04;


import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.*;


public class ProductHibernateTest extends AbstractHibernateClass {


    @Test
    void getProductsSearchFromPrice() throws SQLException {
        //given
        String sql = "SELECT * FROM products WHERE price<'300.0'";
        Statement stmt = getConnection().createStatement();
        int countTableSize = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        Query query = getSession().createSQLQuery("SELECT * FROM products").addEntity(ProductsEntity.class);
        //then
        Assertions.assertEquals(4, countTableSize);
        Assertions.assertEquals(10, query.list().size());
    }

    @ParameterizedTest
    @CsvSource({"GOJIRA ROLL, 300", "VIVA LAS VEGAS ROLL,450", "MINERAL WATER, 50"})
    void getProductsReturnNamePrice(String menu_name, Integer price) throws SQLException {
        //given
        String sql = "SELECT * FROM products WHERE menu_name='" + menu_name + "'";
        Statement stmt = getConnection().createStatement();
        int nameString = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            nameString = rs.getInt(3);
        }
        //then
        Assertions.assertEquals(price, nameString);
    }

    @Test
    void addProductSaveValid() {
        //given
        ProductsEntity product = new ProductsEntity();
        product.setProductId((short) 11);
        product.setMenuName("TOMATO");
        product.setPrice("160");
        //when
        Session session = getSession();
        session.beginTransaction();
        session.persist(product);
        session.getTransaction().getStatus();

        Query query = getSession()
                .createSQLQuery("SELECT * FROM products WHERE product_id=" + 11).addEntity(ProductsEntity.class);

        ProductsEntity productsEntity = (ProductsEntity) query.uniqueResult();
        //then
        Assertions.assertNotNull(productsEntity);
        Assertions.assertEquals("160.0", productsEntity.getPrice());
        Assertions.assertEquals(1, query.list().size());
    }


}



