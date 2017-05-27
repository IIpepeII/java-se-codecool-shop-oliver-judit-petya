package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.jdbcImplementation.ProductCategoryDaoJDBC;
import com.codecool.shop.dao.jdbcImplementation.ProductDaoJDBC;
import com.codecool.shop.dao.jdbcImplementation.SupplierDaoJDBC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;


/**
 * ProductController class rendering the index page and showing all the
 * Products stored in the storage.
 */
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private static SupplierDao productSupplierDataStore = SupplierDaoJDBC.getInstance();
    private static ProductDao productDataStore = ProductDaoJDBC.getInstance();
    private static ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();

    /**
     * This method renders the index page with all products details.
     * @param req Request object for the session.
     * @param res Response object for Spark.
     * @return Spark ModelAndView
     */
    public static ModelAndView renderProducts(Request req, Response res) {
        logger.info("GET request - URL: '/' or '/index'");
        req.session(true);

        Map indexRenderParams = paramFiller(req);
        indexRenderParams.put("products", productDataStore.getAll());
        return new ModelAndView(indexRenderParams, "product/index");
    }

    /**
     * This method renders the index page with all information about products filtered by
     * ProductCategory.
     * @param req Request object for paramFiller method.
     * @param res Response object for Spark.
     * @param categoryID to find the given ProductCategory.
     * @return Spark ModelAndView
     */
    public static ModelAndView renderProductsbyCategory(Request req, Response res, int categoryID) {
        Map categoryRenderParams = paramFiller(req);
        categoryRenderParams.put("products", productDataStore.getBy(productCategoryDataStore.find(categoryID)));
        return new ModelAndView(categoryRenderParams, "product/index");
    }

    /**
     /**
     * This method renders the index page with all information about products filtered by
     * Supplier.
     * @param req Request object for paramFiller method.
     * @param res Response object for Spark.
     * @param supplierID to find the given Supplier.
     * @return Spark ModelAndView
     */
    public static ModelAndView renderProductsbySupplier(Request req, Response res, int supplierID) {
        Map supRenderParams = paramFiller(req);
        supRenderParams.put("products", productDataStore.getBy(productSupplierDataStore.find(supplierID)));
        return new ModelAndView(supRenderParams, "product/index");
    }

    /**
     * This method is responsible for filling "params" HashMap with necesarry datas for rendering.
     * @param req Request object for session.
     * @return a HashMap filled with parameters for the rendering methods.
     */
    public static Map paramFiller(Request req) {
        Map params = new HashMap<>();
        params.put("orderQuantity", req.session().attribute("orderQuantity"));
        params.put("categories", productCategoryDataStore.getAll());
        params.put("suppliers", productSupplierDataStore.getAll());
        return params;
    }
}
