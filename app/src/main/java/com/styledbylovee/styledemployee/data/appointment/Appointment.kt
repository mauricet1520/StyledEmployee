package com.styledbylovee.styledemployee.data.appointment
import android.os.Parcel
import android.os.Parcelable

data class Appointment(
    var appointment_id: String? = null,

    var street_address: String? = null,
    var city: String? = null,
    var zip: String? = null,
    var state: String? = null,
    var occasion: String? = null,

    var start_time: String? = null,
    var end_time: String? = null,
    var cost: Long? = null,
    var event: String? = null,
    var budget: String? = null,
    var product_ids: List<String>? = null,
    var image_url: String? = null,

    var setmore_appointment_key: String? = null,
    var setmore_service_name: String? = null,
    var setmore_label: String? = null,
    var setmore_staff_key: String? = null,
    var setmore_service_key: String? = null,
    var setmore_customer_key: String? = null,
    var stylist_id: String? = null,
    var customer_id: String? = null,
    var status: String? = null,
    var user_appointment_time: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(appointment_id)
        parcel.writeString(street_address)
        parcel.writeString(city)
        parcel.writeString(zip)
        parcel.writeString(state)
        parcel.writeString(occasion)
        parcel.writeString(start_time)
        parcel.writeString(end_time)
        parcel.writeValue(cost)
        parcel.writeString(event)
        parcel.writeString(budget)
        parcel.writeStringList(product_ids)
        parcel.writeString(image_url)
        parcel.writeString(setmore_appointment_key)
        parcel.writeString(setmore_service_name)
        parcel.writeString(setmore_label)
        parcel.writeString(setmore_staff_key)
        parcel.writeString(setmore_service_key)
        parcel.writeString(setmore_customer_key)
        parcel.writeString(stylist_id)
        parcel.writeString(customer_id)
        parcel.writeString(status)
        parcel.writeString(user_appointment_time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Appointment> {
        override fun createFromParcel(parcel: Parcel): Appointment {
            return Appointment(
                parcel
            )
        }

        override fun newArray(size: Int): Array<Appointment?> {
            return arrayOfNulls(size)
        }
    }
}
