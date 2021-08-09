package com.styledbylovee.styledemployee.data.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.util.*

class ProductViewModel(app: Application): AndroidViewModel(app)  {
    private val productRepository = ProductRepository(app)
    val transactionData = productRepository.transactionData
    val transactionNumberData = productRepository.transactionNumber

    fun getProductsInTransaction(transaction_number: String) {
        productRepository.getProductsInTransaction(transaction_number)
    }

    fun saveProductInTransaction(transaction: Transaction) {
        productRepository.saveProductInTransaction(transaction)
    }

    fun createTransactionNumber() {
        productRepository.beginTransaction()
    }

    fun deleteProduct(transactionNumber: String?, skuNumber: String?) {
        productRepository.deleteProduct(transactionNumber, skuNumber)
    }
}