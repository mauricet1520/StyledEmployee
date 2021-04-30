package com.styledbylovee.styledemployee.data.appointment

data class AppointmentResponse (
    var response: Boolean? = null,
    var msg: String? = null,
    var data: Data? = null
)

data class Data (
    var appointment: SetmoreAppointment? = null,
    var appointments: List<SetmoreAppointment>? = null
)