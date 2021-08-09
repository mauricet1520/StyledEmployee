package com.styledbylovee.styledemployee.data.appointment

import com.styledbylovee.styledemployee.data.staff.FetchAllStaffResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AppointmentService {

    @POST("/updateAppointmentInFB")
    suspend fun updateAppointment(@Body appointment: AppointmentDTO)

    @GET("/getAppointment")
    suspend fun getAppointment(@Query("appointmentId") appointmentId: String):
            Response<AppointmentDTO>

    @GET("/fetchAllAppointments")
    suspend fun fetchAllAppointments(
            @Query("start_time") start_time: String,
            @Query("end_time") end_time: String,
            @Query("staff_key") staff_key: String): Response<AppointmentResponse>

    @GET("/getAllStaff")
    suspend fun getAllStaff(): Response<FetchAllStaffResponse>

}