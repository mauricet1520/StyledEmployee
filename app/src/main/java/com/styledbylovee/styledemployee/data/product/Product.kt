package com.styledbylovee.styledemployee.data.product

data class Product (
    var date: String? = null,
    var cost: Double? = null,
    var transaction_number: String? = null,
    var store_name: String? = null,
    var item_type: String? = null,
    var item_image_url: String? = null,
    var sku_image_url: String? = null,
    var sku_number: String? = null,
    var name: String? = null,

    var setmore_appointment_key: String? = null,
    var setmore_staff_key: String? = null,
    var setmore_service_key: String? = null,
    var setmore_customer_key: String? = null,
    var firebase_appointment_id: String? = null,
    var firebase_stylist_id: String? = null,
    var firebase_customer_id: String? = null
)