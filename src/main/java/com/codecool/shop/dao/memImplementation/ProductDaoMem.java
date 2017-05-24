package com.codecool.shop.dao.memImplementation;


import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDaoMem implements ProductDao {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private static ProductDaoMem instance = null;
    private List<Product> DATA = new ArrayList<>();

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductDaoMem() {
    }

    public static ProductDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        product.setId(DATA.size() + 1);
        DATA.add(product);
        logger.info("New product: {} now exists in the memory. ", product.getName());
    }

    @Override
    public Product find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        if (DATA.size() > 0 && DATA.contains(find(id))) {
            logger.info("Product '{}' will be removed from the memory.", find(id).getName());
        }
        DATA.remove(find(id));
    }

    public void removeAll() {
        DATA.clear();
        logger.info(" Every product are deleted from the memory");
    }

    @Override
    public List<Product> getAll() {
        return DATA;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        return DATA.stream().filter(t -> t.getSupplier().equals(supplier)).collect(Collectors.toList());
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        return DATA.stream().filter(t -> t.getProductCategory().equals(productCategory)).collect(Collectors.toList());
    }
}
