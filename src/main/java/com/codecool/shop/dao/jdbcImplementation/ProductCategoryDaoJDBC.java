package com.codecool.shop.dao.jdbcImplementation;

import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the ProductCategory table in the database.
 * Its a singleton cause we don't need more connection for handling the table.
 */
public class ProductCategoryDaoJDBC extends JDBCAbstract implements ProductCategoryDao {

    private static ProductCategoryDaoJDBC instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    /**
     * This private constructor method prevents any other class from instantiating.
     */
    private ProductCategoryDaoJDBC() {
    }

    /**
     * This method checks if ProductCategoryDaoJDBC has an instance. If not it instantiate one,
     * and fill the instance field with it, if it already has one, returns that one.
     * @return ProductCategoryDaoJDBC
     */
    public static ProductCategoryDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoJDBC();
        }
        return instance;
    }

    /**
     * This method adds a new product category to the Product Category Table and fill its
     * fields by the given ProductCategory objects fields values.
     * @param productCategory object to fill the table's field by the values of the fields of the objects.
     */
    public void add(ProductCategory productCategory) {
        String insertIntoTable = "INSERT INTO productcategory (name, description, department) VALUES (?,?,?);";

        try {
            preparedStatement = dbConnection.prepareStatement(insertIntoTable);
            preparedStatement.setString(1, productCategory.getName());
            preparedStatement.setString(2, productCategory.getDescription());
            preparedStatement.setString(3, productCategory.getDepartment());
            preparedStatement.executeUpdate();

            // Get the ID of the most recent record and update our supplier
            String findProductCategory = "SELECT id FROM ProductCategory ORDER BY id DESC LIMIT 1;";
            preparedStatement = dbConnection.prepareStatement(findProductCategory);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                productCategory.setId(result.getInt("id"));
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        logger.info("New product category( {} ) now exists in the database.", productCategory.getName());
    }

    /**
     * This method executes a query to find a row in the ProductCategory table its Id,
     * then instantiate a new ProductCategory objects using the values from the row.
     * @param id to find the row.
     * @return ProductCategory object, created by the informations from the database
     */
    public ProductCategory find(int id) {
        String query = "SELECT * FROM ProductCategory WHERE id = ?;";

        try {
            preparedStatement = dbConnection.prepareStatement(query,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY,
                    ResultSet.CLOSE_CURSORS_AT_COMMIT);
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                ProductCategory productCategory = new ProductCategory(
                        result.getString("name"),
                        result.getString("description"),
                        result.getString("department"));
                productCategory.setId(result.getInt("id"));
                return productCategory;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method remove a record from the ProductCategory table used the Id to find it.
     * @param id for finding the right row.
     */
    public void remove(int id) {
        if (getAll().size() > 0 && getAll().contains(find(id))) {
            logger.info("Product category '{}' will be removed from Database.", find(id).getName());
        }
        remove(id, "ProductCategory");
    }

    /**
     * This method executes a query to find all product categories in the table, then
     * collects every new ProductCategory object created by the datas in a List.
     * @return List<ProductCategory> object with every newly created ProductCategory object
     */
    public List<ProductCategory> getAll() {
        String query = "SELECT * FROM ProductCategory";
        List<ProductCategory> productCategoryList = new ArrayList<>();

        try {
            preparedStatement = dbConnection.prepareStatement(query,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY,
                    ResultSet.CLOSE_CURSORS_AT_COMMIT);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                ProductCategory productCategory = new ProductCategory(
                        result.getString("name"),
                        result.getString("description"),
                        result.getString("department"));
                productCategory.setId(result.getInt("id"));
                productCategoryList.add(productCategory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productCategoryList;
    }

    /**
     * This method clean the whole ProductCategory table and delete every
     * records in other tables which have references to records in this table.
     */
    public void removeAll() {
        String removeRecords = "TRUNCATE productcategory CASCADE;";

        try {
            preparedStatement = dbConnection.prepareStatement(removeRecords);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("Product category table is empty!");
    }

}


