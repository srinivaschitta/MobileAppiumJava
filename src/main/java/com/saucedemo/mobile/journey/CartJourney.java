package com.saucedemo.mobile.journey;

import com.saucedemo.mobile.data.DemoProduct;
import com.saucedemo.mobile.screens.CartScreen;
import com.saucedemo.mobile.screens.CatalogScreen;
import com.saucedemo.mobile.screens.ProductDetailsScreen;

public final class CartJourney {

    public CartScreen addProductAndOpenCart(DemoProduct product) {
        ProductDetailsScreen productDetailsScreen = new CatalogScreen().openProduct(product.catalogSearchName());
        productDetailsScreen.addToCart();
        return productDetailsScreen.header().openCart();
    }
}
