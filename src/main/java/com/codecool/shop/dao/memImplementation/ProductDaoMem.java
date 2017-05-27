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

/**
 * This singleton class collects every Product objects in its DATA container.
 * Through its methods we can handle our Product objects.
 */
public class ProductDaoMem implements ProductDao {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private static ProductDaoMem instance = null;
    private List<Product> DATA = new ArrayList<>();

    /**
     * This private constructor method prevents any other class from instantiating.
     */
    private ProductDaoMem() {
    }

    /**
     * This method checks if ProductDaoMem has an instance. If not, it instantiates one,
     * and fill the instance field with it, if it already has one, returns that one.
     * @return ProductDaoMem
     */
    public static ProductDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductDaoMem();
        }
        return instance;
    }

    /**
     * Add the given Productobject to the DATA container.
     * @param product Product object for DATA container     */
    @Override
    public void add(Product product) {
        product.setId(DATA.size() + 1);
        DATA.add(product);
        logger.info("New product: {} now exists in the memory. ", product.getName());
    }

    /**
     * This method find a Product object in the DATA container by the given Id
     * then returns it.
     * @param id to find the Product by its Id in the DATA container
     * @return Product
     */
    @Override
    public Product find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    /**
     * This method removes a Productfrom DATA container by its Id.
     * @param id to find the Product by its Id in the DATA container
     */
    @Override
    public void remove(int id) {
        if (DATA.size() > 0 && DATA.contains(find(id))) {
            logger.info("Product '{}' will be removed from the memory.", find(id).getName());
        }
        DATA.remove(find(id));
    }

    /**
     * This method purges every Product from the DATA container.
     */
    public void removeAll() {
        DATA.clear();
        logger.info(" Every product are deleted from the memory");
    }

    /**
     * This method returns DATA container.
     * @return List<Product>
     */
    @Override
    public List<Product> getAll() {
        return DATA;
    }

    /**
     * This method returns Products have references to the given
     * Supplier object, from DATA container.
     * @param supplier Supplier object with reference to products
     * @return List<Product>
     */
    @Override
    public List<Product> getBy(Supplier supplier) {
        return DATA.stream().filter(t -> t.getSupplier().equals(supplier)).collect(Collectors.toList());
    }

    /**
     * This method returns Products have references to the given
     * ProductCategory object, from DATA container.
     * @param productCategory ProductCategory object with reference to products
     * @return List<Product>
     */
    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        return DATA.stream().filter(t -> t.getProductCategory().equals(productCategory)).collect(Collectors.toList());
    }
}
