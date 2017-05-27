package com.codecool.shop.dao;

import com.codecool.shop.model.Supplier;

import java.util.List;

/**
 * SupplierDao is for handling ProductCategory objects.
 */
public interface SupplierDao {

    /**
     * Adds a Supplier object to the storage.
     * @param supplier a Supplier object to be stored.
     */
    void add(Supplier supplier);

    /**
     * Finds and gives back a Supplier by its Id from the storage.
     * @param id of the Supplier object
     * @return Supplier
     */
    Supplier find(int id);

    /**
     * Removes a Supplier by its id from the storage.
     * @param id id of the Supplier to remove
     */
    void remove(int id);

    /**
     * Removes every stored Supplier objects.
     */
    void removeAll();

    /**
     * Gives back a List of all stored Supplier objects.
     * @return List of Suppliers.
     */
    List<Supplier> getAll();

}
