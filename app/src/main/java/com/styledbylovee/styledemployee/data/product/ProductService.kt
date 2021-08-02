package com.styledbylovee.styledemployee.data.product

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductService {

    @GET("/getProductsInTransaction")
    suspend fun getProductsInTransaction(
        @Query("transaction_number") transaction_number: String): Response<Transaction>

    @POST("/saveProductInTransaction")
    suspend fun saveProductInTransaction(@Body transaction: Transaction)
}