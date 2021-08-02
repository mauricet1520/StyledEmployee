package com.coolreecedev.styledpractice.data.checkout

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.styledbylovee.styledemployee.data.checkout.StripeRequest
import com.styledbylovee.styledemployee.util.LOG_TAG
import com.styledbylovee.styledemployee.util.STRIPE_STYLED_BASE_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CheckoutRepository(val app: Application) {

    val clientSecret = MutableLiveData<String>()

    fun createPayment(stripeRequest: StripeRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            callWebService(stripeRequest)
        }
    }


    @WorkerThread
    suspend fun callWebService(stripeRequest: StripeRequest) {
        if (networkAvailable()) {
            Log.i(LOG_TAG, "Calling WebService: $STRIPE_STYLED_BASE_URL")
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl(STRIPE_STYLED_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val service = retrofit.create(CheckoutService::class.java)

            val serviceData = service.createPaymentIntent(stripeRequest).body()


            clientSecret.postValue(serviceData!!)

        }
    }

    @Suppress("DEPRECATION")
    fun networkAvailable(): Boolean {
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false
    }
}