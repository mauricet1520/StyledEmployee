package com.coolreecedev.styledpractice.data.checkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.styledbylovee.styledemployee.data.checkout.StripeRequest

class CheckoutViewModel(app: Application): AndroidViewModel(app)  {
    private val checkoutRepository = CheckoutRepository(app)
    val clientSecret = checkoutRepository.clientSecret

    fun createPaymentIntent(stripeRequest: StripeRequest) {
        checkoutRepository.createPayment(stripeRequest)
    }
}