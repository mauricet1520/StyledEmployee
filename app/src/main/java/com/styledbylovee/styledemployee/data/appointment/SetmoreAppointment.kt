package com.styledbylovee.styledemployee.data.appointment

import com.google.gson.annotations.SerializedName

data class SetmoreAppointment (
    @SerializedName("key")
    var key: String? = null,
    @SerializedName("start_time")
    var startTime: String? = null,
    @SerializedName("end_time")
    var endTime: String? = null,
    @SerializedName("duration")
    var duration: Int? = null,
    @SerializedName("staff_key")
    var staffKey: String? = null,
    @SerializedName("service_key")
    var serviceKey: String? = null,
    @SerializedName("customer_key")
    var customerKey: String? = null,
    @SerializedName("cost")
    var cost: Int? = null,
    @SerializedName("currency")
    var currency: String? = null,
    @SerializedName("comment")
    var comment: String? = null,
    @SerializedName("label")
    var label: String? = null
)