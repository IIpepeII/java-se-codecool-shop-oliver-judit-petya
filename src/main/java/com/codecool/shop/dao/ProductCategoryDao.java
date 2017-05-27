package com.codecool.shop.dao;

import com.codecool.shop.model.ProductCategory;

import java.util.List;

/**
 * ProductCategoryDao is for handling ProductCategory objects.
 */
public interface ProductCategoryDao {

    /**
     * Adds a ProductCategory object to the storage.
     * @param category a ProductCategory object to be stored.
     */
    void add(ProductCategory category);

    /**
     * Finds and gives back a ProductCategory object by its Id from the storage.
     * @param id of the ProductCategory object
     * @return ProductCategory
     */
    ProductCategory find(int id);

    /**
     * Removes a ProductCategory by its id from the storage.
     * @param id id of the ProductCategory to remove
     */
    void remove(int id);

    /**
     * Removes every stored ProductCategory objects.
     */
    void removeAll();

    /**
     * Gives back a List of all stored ProductCategory objects.
     * @return List of ProductCategories.
     */
    List<ProductCategory> getAll();

}
