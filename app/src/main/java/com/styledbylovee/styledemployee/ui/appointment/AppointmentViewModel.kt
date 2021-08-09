package com.styledbylovee.styledemployee.ui.appointment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.styledbylovee.styledemployee.data.appointment.AppointmentDTO
import com.styledbylovee.styledemployee.data.appointment.AppointmentRepository

class AppointmentViewModel(app: Application): AndroidViewModel(app) {

    private val appointmentRepository =
        AppointmentRepository(
            app
        )
    val setmoreAppointmentData = appointmentRepository.setmoreAppointmentData
    val appointmentData = appointmentRepository.appointmentData
    val staffData = appointmentRepository.staffData

    fun getSetmoreAppointment(staff_key: String) {
        appointmentRepository.fetchAppointments(staff_key)
    }

    fun getAppointment(appointmentId: String) {
        appointmentRepository.getAppointments(appointmentId)
    }

    fun getAllStaff() {
        appointmentRepository.getAllStaff()
    }

    fun updateAppointment(appointmentDTO: AppointmentDTO) {
        appointmentRepository.updateAppointment(appointmentDTO)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}