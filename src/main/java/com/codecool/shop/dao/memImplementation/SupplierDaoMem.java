package com.codecool.shop.dao.memImplementation;

import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This singleton class collects every Supplier objects in its DATA container.
 * Through its methods we can handle our Supplier objects.
 */
public class SupplierDaoMem implements SupplierDao {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private static SupplierDaoMem instance = null;
    private List<Supplier> DATA = new ArrayList<>();

    /**
     * This private constructor method prevents any other class from instantiating.
     */
    private SupplierDaoMem() {
    }

    /**
     * This method checks if supplierDaoMem has an instance. If not, it instantiates one,
     * and fill the instance field with it, if it already has one, gives it back.
     * @return SupplierDaoMem
     */
    public static SupplierDaoMem getInstance() {
        if (instance == null) {
            instance = new SupplierDaoMem();
        }
        return instance;
    }

    /**
     * Add the given Supplier object to the DATA container.
     * @param supplier Supplier object for DATA container
     */
    @Override
    public void add(Supplier supplier) {
        supplier.setId(DATA.size() + 1);
        DATA.add(supplier);
        logger.info("New supplier: {} now exists int the memory.", supplier.getName());
    }

    /**
     * This method find a Supplier object in the DATA container by the given Id
     * then gives it back.
     * @param id to find the Supplier by its Id in the DATA container
     * @return Supplier
     */
    @Override
    public Supplier find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    /**
     * This method removes a Supplier from DATA container by its Id.
     * @param id to find the Supplier by its Id in the DATA container     */
    @Override
    public void remove(int id) {
        if (DATA.size() > 0 && DATA.contains(find(id))) {
            logger.info("Supplier '{}' will be removed from the memory.", find(id).getName());
        }
        DATA.remove(find(id));
    }

    /**
     * This method purges every Supplier from the DATA container.
     */
    public void removeAll() {
        DATA.clear();
        logger.info("Every supplier deleted from the memory");
    }

    /**
     * This method gives back the DATA container.
     * @return List of Supplier objects
     */
    @Override
    public List<Supplier> getAll() {
        return DATA;
    }
}
