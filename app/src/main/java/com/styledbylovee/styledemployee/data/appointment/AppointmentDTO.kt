package com.styledbylovee.styledemployee.data.appointment
data class AppointmentDTO (
    var appointment_id: String? = null,

    var budget: String? = null,
    var street_address: String? = null,
    var city: String? = null,
    var zip: String? = null,
    var state: String? = null,
    var cost: Long? = null,
    var customer_id: String? = null,
    var occasion: String? = null,
    var status: String? = null,

    var start_time: String? = null,
    var end_time: String? = null,
    var image_url: String? = null,

    var setmore_appointment_key: String? = null,
    var setmore_service_name: String? = null,
    var setmore_label: String? = null,
    var setmore_staff_key: String? = null,
    var setmore_service_key: String? = null,
    var setmore_customer_key: String? = null,
    var stylist_id: String? = null,
    var user_appointment_time: String? = null,
    var product_ids: List<String>? = null
) {
//    val imageUrl
//        get() = STYLE_SERVICE_BASE_URL

}