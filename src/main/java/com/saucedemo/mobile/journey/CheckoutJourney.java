package com.saucedemo.mobile.journey;

import com.saucedemo.mobile.data.DemoCheckoutForm;
import com.saucedemo.mobile.data.DemoProduct;
import com.saucedemo.mobile.data.DemoUser;
import com.saucedemo.mobile.screens.CartScreen;
import com.saucedemo.mobile.screens.CheckoutCompleteScreen;

public final class CheckoutJourney {

    public CheckoutCompleteScreen buyProduct(
            DemoUser user,
            DemoProduct product,
            DemoCheckoutForm checkoutForm) {
        new LoginJourney().loginWith(user);
        CartScreen cartScreen = new CartJourney().addProductAndOpenCart(product);
        return cartScreen.proceedToCheckout()
                .submitAddress(checkoutForm)
                .submitPayment(checkoutForm)
                .placeOrder();
    }
}
