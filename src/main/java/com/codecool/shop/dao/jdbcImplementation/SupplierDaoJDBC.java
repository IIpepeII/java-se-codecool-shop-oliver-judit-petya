package com.codecool.shop.dao.jdbcImplementation;

import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the Supplier table in the database.
 * Its a singleton cause we don't need more connection for handling the table.
 */
public class SupplierDaoJDBC extends JDBCAbstract implements SupplierDao {

    private static SupplierDaoJDBC instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    /**
     * This private constructor method prevents any other class from instantiating.
     */
    private SupplierDaoJDBC() {
    }

    /**
     * This method checks if SupplierDaoJDBC has an instance. If not, it instantiates one,
     * and fill the instance field with it, if it already has one, gives it back.
     * @return SupplierDaoJDBC
     */
    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        return instance;
    }

    /**
     * This method adds a new product category to the Product Category Table and fill its
     * fields by the field values of the given Supplier object.
     * @param supplier object to fill the table's field by the values of the fields of the objects.
     */
    public void add(Supplier supplier) {
        String insertIntoTable = "INSERT INTO Supplier (name, description) VALUES (?,?);";
        try {
            // Adding record to DB
            preparedStatement = dbConnection.prepareStatement(insertIntoTable);
            preparedStatement.setString(1, supplier.getName());
            preparedStatement.setString(2, supplier.getDescription());
            preparedStatement.executeUpdate();

            // Get the ID of the most recent record and update our supplier
            String findSupplier = "SELECT id FROM Supplier ORDER BY id DESC LIMIT 1;";
            preparedStatement = dbConnection.prepareStatement(findSupplier);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                supplier.setId(result.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("New supplier: {} now exists in the database.", supplier.getName());

    }

    /**
     * This method executes a query to find a row in the Supplier table its Id,
     * then instantiate a new Supplier objects using the values from the row.
     * @param id to find the row.
     * @return Supplier object, created by the informations from the database
     */
    public Supplier find(int id) {
        String query = "SELECT * FROM Supplier WHERE id = ?;";
        try {
            preparedStatement = dbConnection.prepareStatement(query,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY,
                    ResultSet.CLOSE_CURSORS_AT_COMMIT);
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                Supplier supplier = new Supplier(
                        result.getString("name"),
                        result.getString("description"));
                supplier.setId(result.getInt("id"));
                return supplier;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method remove a record from the Supplier table used the Id to find it.
     * @param id for finding the right row.
     */
    public void remove(int id) {
        if (getAll().size() > 0 && getAll().contains(find(id))) {
            logger.info("Supplier: {} removed from Database.", find(id).getName());
        }
        remove(id, "Supplier");
    }

    /**
     * This method executes a query to find all suppliers in the table, then
     * collects every new Supplier object created by the datas in a List.
     * @return List of Supplier objects with every newly created Supplier object
     */
    public List<Supplier> getAll() {
        String query = "SELECT * FROM supplier";
        List<Supplier> supplierList = new ArrayList<>();
        try {
            preparedStatement = dbConnection.prepareStatement(query,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY,
                    ResultSet.CLOSE_CURSORS_AT_COMMIT);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Supplier supplier = new Supplier(
                        result.getString("name"),
                        result.getString("description"));
                supplier.setId(result.getInt("id"));
                supplierList.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplierList;
    }

    /**
     * This method clean the whole Supplier table and delete every
     * records in other tables which have references to records in this table.
     */
    public void removeAll() {
        try {
            String removeRecords = "TRUNCATE Supplier CASCADE;";
            preparedStatement = dbConnection.prepareStatement(removeRecords);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("Supplier table is empty!");
    }
}
