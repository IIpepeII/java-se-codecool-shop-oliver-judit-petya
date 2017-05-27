package com.codecool.shop.dao.jdbcImplementation;

import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the Product table in the database.
 * Its a singleton cause we don't need more connection for handling the table.
 */
public class ProductDaoJDBC extends JDBCAbstract implements ProductDao {

    private static ProductDaoJDBC instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private SupplierDaoJDBC supplierDaoJdbc = SupplierDaoJDBC.getInstance();
    private ProductCategoryDaoJDBC productCategoryDaoJdbc = ProductCategoryDaoJDBC.getInstance();

    /**
     * This private constructor method prevents any other class from instantiating.
     */
    private ProductDaoJDBC() {
    }

    /**
     * This method checks if ProductDaoJDBC has an instance. If not, it instantiates one,
     * and fill the instance field with it, if it already has one, returns that one.
     * @return ProductDaoJDBC
     */
    public static ProductDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
        }
        return instance;
    }

    /**
     * This method adds a new product to the Product Table and fill its
     * fields by field values of the given Product object.
     * @param product object to fill the table's field by the values of the fields of the objects.
     */
    public void add(Product product) {
        String insertIntoTable = "INSERT INTO product (name, description, currency, default_price, supplier_id, product_category_id) VALUES (?,?,?,?,?,?);";
        try {
            // Add record to DB
            preparedStatement = dbConnection.prepareStatement(insertIntoTable);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setString(3, product.getDefaultCurrency().toString());
            preparedStatement.setFloat(4, product.getDefaultPrice());
            preparedStatement.setInt(5, product.getSupplier().getId());
            preparedStatement.setInt(6, product.getProductCategory().getId());
            preparedStatement.executeUpdate();

            // Get the ID of the most recent record and update our supplier
            String findProduct = "SELECT id FROM Product ORDER BY id DESC LIMIT 1;";
            preparedStatement = dbConnection.prepareStatement(findProduct);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                product.setId(result.getInt("id"));
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        logger.info("New product: {} now exists in the database.", product.getName());
    }

    /**
     * This method executes a query to find a row in the Product table its Id,
     * then instantiate a new Product objects using the values from the row.
     * @param id to find the row.
     * @return Product object, created by the informations from the database
     */
    public Product find(int id) {
        String query = "SELECT * FROM Product WHERE id = ?;";
        try {
            preparedStatement = dbConnection.prepareStatement(query,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY,
                    ResultSet.CLOSE_CURSORS_AT_COMMIT);
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                Product product = new Product(
                        result.getString("name"),
                        result.getFloat("default_price"),
                        result.getString("currency"),
                        result.getString("description"),
                        productCategoryDaoJdbc.find(result.getInt("product_category_id")),
                        supplierDaoJdbc.find(result.getInt("supplier_id"))
                );
                product.setId(result.getInt("id"));
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method remove a record from the Product table used the Id to find it.
     * @param id for finding the right row.
     */
    public void remove(int id) {
        if (getAll().size() > 0 && getAll().contains(find(id))) {
            logger.info("Product {} will be removed from Database.", find(id).getName());
        }
        remove(id, "Product");
    }

    /**
     * This method clean the whole Product table and delete every
     * records in other tables which have references to records in this table.
     */
    public void removeAll() {
        try {
            String removeRecords = "TRUNCATE Product CASCADE;";
            preparedStatement = dbConnection.prepareStatement(removeRecords);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("Product table is empty.");
    }

    /**
     * This method executes a query to find all products in the table, then
     * collects every new Product object created by the datas in a List.
     * @return List<Product> object with every newly created Product object
     */
    public List<Product> getAll() {
        String query = "SELECT * FROM Product;";
        List<Product> productList = new ArrayList<>();

        try {
            preparedStatement = dbConnection.prepareStatement(query,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY,
                    ResultSet.CLOSE_CURSORS_AT_COMMIT);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Product product = new Product(
                        result.getString("name"),
                        result.getFloat("default_price"),
                        result.getString("currency"),
                        result.getString("description"),
                        productCategoryDaoJdbc.find(result.getInt("product_category_id")),
                        supplierDaoJdbc.find(result.getInt("supplier_id"))
                );
                product.setId(result.getInt("id"));
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    /**
     * This method executes a query to find all products in the table,
     * which have a reference to the given Supplier object from the Supplier table,
     * then collects every new Product object created by the datas in a List.
     * @param supplier to find products with the reference to the Supplier table record
     *                 with similar fields to the given Supplier object
     * @return List<Product> object with every newly created Product object
     */
    public List<Product> getBy(Supplier supplier) {
        String query = "SELECT * FROM Product WHERE supplier_id = ?;";
        List<Product> productList = new ArrayList<>();

        try {
            preparedStatement = dbConnection.prepareStatement(query,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY,
                    ResultSet.CLOSE_CURSORS_AT_COMMIT);
            preparedStatement.setInt(1, supplier.getId());
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Product product = new Product(
                        result.getString("name"),
                        result.getFloat("default_price"),
                        result.getString("currency"),
                        result.getString("description"),
                        productCategoryDaoJdbc.find(result.getInt("product_category_id")),
                        supplierDaoJdbc.find(result.getInt("supplier_id"))
                );
                product.setId(result.getInt("id"));
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    /**
     * This method executes a query to find all products in the table,
     * which have a reference to the given ProductCategory object from the ProductCategory table,
     * then collects every new Product object created by the datas in a List.
     * @param productCategory to find products with the reference to the ProductCategory table record
     *                 with similar fields to the given ProductCategory object
     * @return List<Product> object with every newly created Product object
     */
    public List<Product> getBy(ProductCategory productCategory) {
        String query = "SELECT * FROM Product WHERE product_category_id = ?;";
        List<Product> productList = new ArrayList<>();

        try {
            preparedStatement = dbConnection.prepareStatement(query,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY,
                    ResultSet.CLOSE_CURSORS_AT_COMMIT);
            preparedStatement.setInt(1, productCategory.getId());
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Product product = new Product(
                        result.getString("name"),
                        result.getFloat("default_price"),
                        result.getString("currency"),
                        result.getString("description"),
                        productCategoryDaoJdbc.find(result.getInt("product_category_id")),
                        supplierDaoJdbc.find(result.getInt("supplier_id"))
                );
                product.setId(result.getInt("id"));
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }
}


