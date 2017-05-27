package com.codecool.shop.dao;

import com.codecool.shop.model.Order;

import java.util.List;

/**
 * OrderDao is for handling Order objects.
 */
public interface OrderDao {

    /**
     * Adds an Order object to the storage.
     * @param order The Order object to be stored.
     */
    void add(Order order);

    /**
     * Finds and returns an Order object by its Id from the storage.
     * @param id of the Order
     * @return Order
     */
    Order find(int id);

    /**
     * Removes an Order by its id from the storage.
     * @param id of the Order to remove
     */
    void remove(int id);

    /**
     * Returns a List of every stored Order objects.
     * @return List of Orders.
     */
    List<Order> getAll();
}
