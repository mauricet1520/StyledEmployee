package com.styledbylovee.styledemployee.data.product

import retrofit2.Response
import retrofit2.http.*

interface ProductService {

    @GET("/getProductsInTransaction")
    suspend fun getProductsInTransaction(
        @Query("transaction_number") transaction_number: String): Response<Transaction>

    @POST("/saveProductInTransaction")
    suspend fun saveProductInTransaction(@Body transaction: Transaction)

    @PUT("/deleteProductInTransaction")
    suspend fun deleteProduct(transactionNumber: String, skuNumber: String?): Response<Transaction?>
}