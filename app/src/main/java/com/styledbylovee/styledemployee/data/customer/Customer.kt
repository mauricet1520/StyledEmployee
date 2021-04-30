package com.styledbylovee.styledemployee.data.customer

import android.os.Parcel
import android.os.Parcelable

data class Customer(
    var uid: String? = null,
    var stripe_customer_id: String? = null,
    var setmore_customer_id: String? = null,
    var hubspot_customer_id: String? = null,

    var first_name: String? = null,
    var last_name: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var address: String? = null,
    var city: String? = null,
    var state: String? = null,
    var zip: String? = null,

    var appointment_ids : MutableList<String>? = mutableListOf(),
    var product_ids : List<String>? = null,

    var image_url: String? = null,
    var height: String? = null,
    var body_type: MutableList<String>? = mutableListOf(),
    var clothing_type: String? = null,

    var bottom_size: String? = null,
    var dress_size: String? = null,
    var jumper_romper_size: String? = null,
    var top_size: String? = null,
    var blazer_size: String? = null,
    var dress_shirt_size: String? = null,
    var pants_size: String? = null,
    var shirt_size: String? = null,

    var preferred_stores: List<String>? = null,
    var colors: MutableList<String>? = mutableListOf(),
    var print_patterns: MutableList<String>? = mutableListOf()
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
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
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(stripe_customer_id)
        parcel.writeString(setmore_customer_id)
        parcel.writeString(hubspot_customer_id)
        parcel.writeString(first_name)
        parcel.writeString(last_name)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(zip)
        parcel.writeStringList(appointment_ids)
        parcel.writeStringList(product_ids)
        parcel.writeString(image_url)
        parcel.writeString(height)
        parcel.writeStringList(body_type)
        parcel.writeString(clothing_type)
        parcel.writeString(bottom_size)
        parcel.writeString(dress_size)
        parcel.writeString(jumper_romper_size)
        parcel.writeString(top_size)
        parcel.writeString(blazer_size)
        parcel.writeString(dress_shirt_size)
        parcel.writeString(pants_size)
        parcel.writeString(shirt_size)
        parcel.writeStringList(preferred_stores)
        parcel.writeStringList(colors)
        parcel.writeStringList(print_patterns)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Customer> {
        override fun createFromParcel(parcel: Parcel): Customer {
            return Customer(parcel)
        }

        override fun newArray(size: Int): Array<Customer?> {
            return arrayOfNulls(size)
        }
    }
}