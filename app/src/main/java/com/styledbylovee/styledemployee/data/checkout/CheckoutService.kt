package com.coolreecedev.styledpractice.data.checkout

import com.styledbylovee.styledemployee.data.checkout.StripeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CheckoutService {

    @POST("createPaymentIntent")
    suspend fun createPaymentIntent(@Body stripeRequest: StripeRequest): Response<String>
}