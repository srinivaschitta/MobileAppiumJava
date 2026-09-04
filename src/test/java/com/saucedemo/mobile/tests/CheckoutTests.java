package com.saucedemo.mobile.tests;

import com.saucedemo.mobile.assertion.CheckoutAssert;
import com.saucedemo.mobile.data.DemoCheckoutForm;
import com.saucedemo.mobile.data.DemoProduct;
import com.saucedemo.mobile.data.DemoUser;
import com.saucedemo.mobile.journey.CheckoutJourney;
import com.saucedemo.mobile.screens.CheckoutCompleteScreen;
import org.testng.annotations.Test;

public class CheckoutTests extends BaseTest {

    @Test(
            groups = {"checkout"},
            description = "Given a logged-in user with an item in the cart, when they check out, then the order is confirmed")
    public void validateCheckoutCompletesWithDemoAddress() {
        DemoUser standardUser = DemoUser.standardUser();
        DemoProduct backpack = DemoProduct.backpack();
        DemoCheckoutForm checkoutForm = DemoCheckoutForm.rebeccaWinter();

        CheckoutCompleteScreen checkoutCompleteScreen = new CheckoutJourney()
                .buyProduct(standardUser, backpack, checkoutForm);

        CheckoutAssert.assertOrderIsComplete(checkoutCompleteScreen);
    }
}
