package com.styledbylovee.styledemployee.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.styledbylovee.styledemployee.data.appointment.AppointmentRepository

class AppointDetailViewModel(app: Application) : AndroidViewModel(app) {
    private val appointmentRepository =
        AppointmentRepository(
            app
        )
    val appointmentData = appointmentRepository.appointmentData


    fun getAppointment(appointmentId: String) {
        appointmentRepository.getAppointments(appointmentId)
    }
}