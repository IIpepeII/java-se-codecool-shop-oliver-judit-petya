package com.codecool.shop.dao.memImplementation;

import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This singleton class collects every Order objects in its DATA container.
 * Through its methods we can handle our Order objects.
 */
public class OrderDaoMem implements OrderDao {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private static OrderDaoMem instance = null;
    private List<Order> DATA = new ArrayList<>();

    /**
     * This private constructor method prevents any other class from instantiating.
     */
    private OrderDaoMem() {
    }

    /**
     * This method checks if OrderDaoMem has an instance. If not, it instantiates one,
     * and fill the instance field with it, if it already has one, gives it back.
     * @return OrderDaoMem
     */
    public static OrderDaoMem getInstance() {
        if (instance == null) {
            instance = new OrderDaoMem();
        }
        return instance;
    }

    /**
     * This method gives back the DATA container.
     * @return List of Order objects
     */
    @Override
    public List<Order> getAll() {
        return DATA;
    }

    /**
     * Add the given Order object to the DATA container.
     * @param order Order object for DATA container
     */
    @Override
    public void add(Order order) {
        order.setId(DATA.size() + 1);
        DATA.add(order);
        logger.info("Order Id:{} now exists in the memory. ", order.getId());
    }

    /**
     * This method find a Order object in the DATA container by the given Id
     * then gives it back.
     * @param id to find the Order by its Id in the DATA container
     * @return Order
     */
    @Override
    public Order find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    /**
     * This method removes a Order from DATA container by its Id.
     * @param id to find the Order by its Id in the DATA container
     */
    @Override
    public void remove(int id) {
        logger.info("Order Id:{} will be removed from memory.", find(id).getId());
        DATA.remove(find(id));
    }
}
