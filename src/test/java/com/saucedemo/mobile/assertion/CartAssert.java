package com.saucedemo.mobile.assertion;

import com.saucedemo.mobile.data.DemoProduct;
import com.saucedemo.mobile.screens.CartScreen;

import static org.assertj.core.api.Assertions.assertThat;

public final class CartAssert {

    private CartAssert() {
    }

    public static void assertProductIsInCart(CartScreen cartScreen, DemoProduct product) {
        assertThat(cartScreen.isLoaded()).isTrue();
        assertThat(cartScreen.hasLineItems()).isTrue();
        boolean foundByFullName = cartScreen.hasProduct(product.cartDisplayName());
        boolean foundByShortName = cartScreen.hasProduct(product.catalogSearchName());
        assertThat(foundByFullName || foundByShortName)
                .as("cart should contain " + product.cartDisplayName())
                .isTrue();
    }
}
