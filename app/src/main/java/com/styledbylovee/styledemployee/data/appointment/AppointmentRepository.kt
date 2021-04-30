package com.styledbylovee.styledemployee.data.appointment
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson

import com.google.gson.GsonBuilder
import com.styledbylovee.styledemployee.util.LOG_TAG
import com.styledbylovee.styledemployee.util.STRIPE_STYLED_BASE_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppointmentRepository(val app: Application) {

    val setmoreAppointmentData = MutableLiveData<List<SetmoreAppointment>>()
    val appointmentData = MutableLiveData<AppointmentDTO>()

    fun fetchAppointments(staff_key: String) {
        CoroutineScope(Dispatchers.IO).launch {
            appointmentWebService(staff_key)
        }
    }

    fun getAppointments(appointmentId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            getAppointmentWebService(appointmentId)
        }
    }

    @WorkerThread
    suspend fun appointmentWebService(staff_key: String) {
        if (networkAvailable()) {

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = buildRetrofit(gson)

            val service = retrofit.create(AppointmentService::class.java)

            val appointmentResponse = service.fetchAllAppointments(
                start_time = "12-02-2020",
                end_time = "12-02-2022",
            staff_key = staff_key)

            if(appointmentResponse.isSuccessful) {
                Log.i(LOG_TAG, "Response: ${appointmentResponse.isSuccessful} Code: ${appointmentResponse.code()}")
                val appointmentDTOList = appointmentResponse
                    .body()?.data?.appointments ?: emptyList()
                setmoreAppointmentData.postValue(appointmentDTOList)
            }else {
                Log.i(LOG_TAG, "Response: ${appointmentResponse.isSuccessful} Code: ${appointmentResponse.code()}")
            }
        }
    }

    @WorkerThread
    suspend fun getAppointmentWebService(appointmentId: String) {
        if (networkAvailable()) {

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = buildRetrofit(gson)

            val service = retrofit.create(AppointmentService::class.java)

            val response = service.getAppointment(appointmentId)

            if (response.isSuccessful) {
                Log.i(LOG_TAG, "Response getAppointment: ${response.isSuccessful} Code: ${response.code()}")
                appointmentData.postValue(response.body())
            }else {
                Log.i(LOG_TAG, "Response getAppointment: ${response.isSuccessful} Code: ${response.code()}")
            }

        }
    }

    private fun buildRetrofit(gson: Gson?): Retrofit {
        return Retrofit.Builder()
            .baseUrl(STRIPE_STYLED_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Suppress("DEPRECATION")
    fun networkAvailable(): Boolean {
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false
    }
}