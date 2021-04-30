package com.styledbylovee.styledemployee.data.customer

import com.styledbylovee.styledemployee.data.customer.Customer
import com.styledbylovee.styledemployee.data.customer.CustomerDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CustomerService {
    @POST("/customer")
    suspend fun addCustomer(@Body customer: Customer
    ): Response<Customer>

    @GET("/getCustomer")
    suspend fun getCustomer(@Query("customerId") uid: String): Response<CustomerDTO>
}