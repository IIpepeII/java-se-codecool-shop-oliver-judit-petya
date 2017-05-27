package com.codecool.shop.dao.memImplementation;


import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This singleton class collects every ProductCategory objects in its DATA container.
 * Through its methods we can handle our ProductCategory objects.
 */
public class ProductCategoryDaoMem implements ProductCategoryDao {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private static ProductCategoryDaoMem instance = null;
    private List<ProductCategory> DATA = new ArrayList<>();

    /**
     * This private constructor method prevents any other class from instantiating.
     */
    private ProductCategoryDaoMem() {
    }

    /**
     * This method checks if ProductCategoryDaoMem has an instance. If not, it instantiates one,
     * and fill the instance field with it, if it already has one, gives it back.
     * @return ProductCategoryDaoMem
     */
    public static ProductCategoryDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoMem();
        }
        return instance;
    }

    /**
     * Add the given ProductCategory object to the DATA container.
     * @param category ProductCategory object for DATA container
     */
    @Override
    public void add(ProductCategory category) {
        category.setId(DATA.size() + 1);
        DATA.add(category);
        logger.info("New product category: {} now exists in memory. ", category.getName());
    }

    /**
     * This method find a ProductCategory object in the DATA container by the given Id
     * then gives it back.
     * @param id to find the ProductCategory by its Id in the DATA container
     * @return ProductCategory
     */
    @Override
    public ProductCategory find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    /**
     * This method removes a ProductCategory from DATA container by its Id.
     * @param id to find the ProductCategory by its Id in the DATA container
     */
    @Override
    public void remove(int id) {
        if (DATA.size() > 0 && DATA.contains(find(id))) {
            logger.info("Product category: '{}' will be removed from memory.", find(id).getName());
        }
        DATA.remove(find(id));
    }

    /**
     * This method gives back the DATA container.
     * @return List of ProductCategory objects
     */
    @Override
    public List<ProductCategory> getAll() {
        return DATA;
    }

    /**
     * This method purges every ProductCategory from the DATA container.
     */
    public void removeAll() {
        DATA.clear();
        logger.info("Every product category deleted from the memory.");
    }

}
