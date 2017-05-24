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

public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private static OrderDaoMem orderList = OrderDaoMem.getInstance();

    private static void updateSession(Request req, Order currentOrder) {
        req.session().attribute("orderQuantity", currentOrder.getOrderQuantity());
        req.session().attribute("orderPrice", currentOrder.getOrderPrice());
        logger.info("Current order quantity: {} price: {} added to session. ", currentOrder.getOrderQuantity(), currentOrder.getOrderPrice());
    }

    private static LineItem returnLineItemFromReq(Request req) {
        String productIdStr = req.queryParams("prodId");
        String productQuantityStr = req.queryParams("quantity");
        int productQuantityInt = Integer.parseInt(productQuantityStr);
        int productIdInt = Integer.parseInt(productIdStr);
        return new LineItem(ProductDaoJDBC.getInstance().find(productIdInt), productQuantityInt);
    }

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
