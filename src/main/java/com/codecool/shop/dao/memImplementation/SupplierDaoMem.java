package com.codecool.shop.dao.memImplementation;

import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SupplierDaoMem implements SupplierDao {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private static SupplierDaoMem instance = null;
    private List<Supplier> DATA = new ArrayList<>();

    /* A private Constructor prevents any other class from instantiating.
     */
    private SupplierDaoMem() {
    }

    public static SupplierDaoMem getInstance() {
        if (instance == null) {
            instance = new SupplierDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) {
        supplier.setId(DATA.size() + 1);
        DATA.add(supplier);
        logger.info("New supplier: {} now exists int the memory.", supplier.getName());
    }

    @Override
    public Supplier find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        if (DATA.size() > 0 && DATA.contains(find(id))) {
            logger.info("Supplier '{}' will be removed from the memory.", find(id).getName());
        }
        DATA.remove(find(id));
    }

    public void removeAll() {
        DATA.clear();
        logger.info("Every supplier deleted from the memory");
    }

    @Override
    public List<Supplier> getAll() {
        return DATA;
    }
}
