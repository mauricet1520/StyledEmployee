package com.styledbylovee.styledemployee.ui.appointment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.styledbylovee.styledemployee.data.appointment.AppointmentRepository

class AppointmentViewModel(app: Application): AndroidViewModel(app) {

    private val appointmentRepository =
        AppointmentRepository(
            app
        )
    val setmoreAppointmentData = appointmentRepository.setmoreAppointmentData
    val appointmentData = appointmentRepository.appointmentData

    fun getSetmoreAppointment(staff_key: String) {
        appointmentRepository.fetchAppointments(staff_key)
    }

    fun getAppointment(appointmentId: String) {
        appointmentRepository.getAppointments(appointmentId)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}