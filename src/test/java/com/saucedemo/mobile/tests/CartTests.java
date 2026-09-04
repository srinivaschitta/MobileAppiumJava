package com.saucedemo.mobile.tests;

import com.saucedemo.mobile.assertion.CartAssert;
import com.saucedemo.mobile.data.DemoProduct;
import com.saucedemo.mobile.journey.CartJourney;
import com.saucedemo.mobile.screens.CartScreen;
import org.testng.annotations.Test;

public class CartTests extends BaseTest {

    @Test(
            groups = {"cart", "smoke"},
            description = "Given the catalog, when a backpack is added, then the cart lists that item")
    public void validateCatalogItemAppearsInCart() {
        DemoProduct backpack = DemoProduct.backpack();

        CartScreen cartScreen = new CartJourney().addProductAndOpenCart(backpack);

        CartAssert.assertProductIsInCart(cartScreen, backpack);
    }
}
