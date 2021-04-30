package com.styledbylovee.styledemployee.data.customer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.styledbylovee.styledemployee.data.customer.Customer
import com.styledbylovee.styledemployee.data.customer.CustomerRepository

class CustomerViewModel(app: Application): AndroidViewModel(app) {

    private val customerRepo = CustomerRepository(app)

    val customerAppointmentData = customerRepo.customerAppointmentData

    fun addCustomer(customer: Customer) {
        customerRepo.createCustomer(customer)
    }

    fun getCustomerAppointment(uid: String) {
        customerRepo.getCustomerAppointment(uid)
    }
}