package com.codecool.shop.controller;

import com.codecool.shop.dao.jdbcImplementation.ProductDaoJDBC;
import com.codecool.shop.dao.memImplementation.OrderDaoMem;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Order;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

/**
 * OrderController class handling orders. Orders contain products, choosed by the customer.
 * It stores all data in an OrderDaoMem and put them in the session.
 */
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private static OrderDaoMem orderList = OrderDaoMem.getInstance();

    /**
     * This method updates the current sessions orderQuantity and orderPrice.
     * @param req the Request parameter is for session handling.
     * @param currentOrder contains all information about the order.
     */
    private static void updateSession(Request req, Order currentOrder) {
        req.session().attribute("orderQuantity", currentOrder.getOrderQuantity());
        req.session().attribute("orderPrice", currentOrder.getOrderPrice());
        logger.info("Current order quantity: {} price: {} added to session. ", currentOrder.getOrderQuantity(), currentOrder.getOrderPrice());
    }

    /**
     * This method creates a LineItem based on the request. Searches the product,
     * gets the needed quantity, then instantiates and gives back a LineItem.
     * @param req Request object for contains information about the product and its quantity
     * @return LineItem
     */
    private static LineItem returnLineItemFromReq(Request req) {
        String productIdStr = req.queryParams("prodId");
        String productQuantityStr = req.queryParams("quantity");
        int productQuantityInt = Integer.parseInt(productQuantityStr);
        int productIdInt = Integer.parseInt(productIdStr);
        return new LineItem(ProductDaoJDBC.getInstance().find(productIdInt), productQuantityInt);
    }

    /**
     * This method gives back an Order instance. Create or find an order by the orderId
     * (if it exists) and put it in the current session.
     * @param req Request object to handle session.
     * @return Order
     */
    private static Order findCurrentOrder(Request req) {
        Order currentOrder = new Order();
        if (!req.session().attributes().contains("orderId")) {
            orderList.add(currentOrder);
            req.session().attribute("orderId", currentOrder.getId());
        } else {
            int orderId = req.session().attribute("orderId");
            currentOrder = orderList.find(orderId);
        }
        return currentOrder;
    }

    /**
     * AddToCart() frist instantiate a LineItem based on the request, finds the current Order, put the LineItem into the Order,
     * Updates the session. Then it also creates a json object from the quantityof all objects
     * existing in the current order, and send a json object to a Javascript method, to refresh
     * the Cart view by Ajax.
     * @param req Request object to handle session.
     * @param res Response object is needed by Spark.
     * @return JSON Object for the AJAX
     */
    public static JSONObject addToCart(Request req, Response res) {
        logger.info("Adding to order initialized");
        LineItem selectedItem = returnLineItemFromReq(req);
        Order currentOrder = findCurrentOrder(req);
        currentOrder.addLineItem(selectedItem);
        logger.info("Product:{} quantity:{} to order no. {} added to cart", selectedItem.getProduct().getName(), selectedItem.getQuantity(), currentOrder.getId());
        updateSession(req, currentOrder);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("numOfLineItems", currentOrder.getOrderQuantity());
        res.type("application/json");
        return jsonObj;
    }
}
