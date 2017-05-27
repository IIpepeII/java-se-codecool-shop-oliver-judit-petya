package com.codecool.shop.dao;

import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.List;

/**
 * ProductDao is for handling ProductCategory objects.
 */
public interface ProductDao {

    /**
     * Adds a Product object to the storage.
     * @param product a Product object to be stored.
     */
    void add(Product product);

    /**
     * Finds and returns a Product object by its Id from the storage.
     * @param id of the Product object
     * @return Product
     */
    Product find(int id);

    /**
     * Removes a Product by its id from the storage.
     * @param id id of the Product to remove
     */
    void remove(int id);

    /**
     * Removes every stored Product objects.
     */
    void removeAll();

    /**
     * Gives back a List of all stored Product objects.
     * @return List of Products
     */
    List<Product> getAll();

    /**
     * Gives back all Products with a reference to the given Supplier.
     * @param supplier the target Supplier
     * @return List of Products
     */
    List<Product> getBy(Supplier supplier);

    /**
     * Gives back all Products with a reference to the given ProductCategory.
     * @param productCategory the target ProductCategory
     * @return List of Products
     */
    List<Product> getBy(ProductCategory productCategory);

}
