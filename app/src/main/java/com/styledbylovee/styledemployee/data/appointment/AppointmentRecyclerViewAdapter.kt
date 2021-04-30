package com.styledbylovee.styledemployee.data.appointment

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.styledbylovee.styledemployee.R
import com.styledbylovee.styledemployee.util.LOG_TAG
import java.text.SimpleDateFormat
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class AppointmentRecyclerViewAdapter(val context: Context,
                                     private val values: List<SetmoreAppointment>,
                                     val itemListener: AppointmentItemListener,
                                     val checkoutItemListener: CheckoutItemListener
) : RecyclerView.Adapter<AppointmentRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view  = inflater.inflate(R.layout.appointment_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val d = SimpleDateFormat("yyyy-MM-dd'T'H:mm", Locale.US).parse(item.startTime!!)

        Log.i(LOG_TAG, "$d")
        Log.i(LOG_TAG, "Start time ${item.startTime}")
        holder.date.text = d.toString()
        when(item.serviceKey) {
            "s5c2c44e31f92d7086b35e71316c72a1454778402" ->  holder.serviceName.text = "Shop & Style"
            "s735b953ea6365d8a320e45fd13ec1dc2bd6a0401" ->  holder.serviceName.text = "Shop your Closet"
            "s0f27750942522cbbc953aa6ea01daf169f4c5d4c" -> holder.serviceName.text = "Virtual Service"
            "s293a092281466b5127666d4c50be75bbd17a49f1" -> holder.serviceName.text = "Seasonal Style"
        }

        holder.viewDetailsButton.setOnClickListener {
            itemListener.onItemClick(item)
        }

        holder.checkoutButton.setOnClickListener {
            checkoutItemListener.onCheckoutItemClick(item)
        }
    }

//    private fun loadPhotoUrl(
//        item: AppointmentDTO,
//        holder: ViewHolder,
//        serviceUrl: String
//
//    ) {
//        Glide.with(context)
//            .load(item.imageUrl + serviceUrl)
//            .into(holder.styleImage)
//
//    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView) {

        val cv: CardView = itemView.findViewById(R.id.card)
        val serviceName: TextView = itemView.findViewById<TextView>(R.id.serviceNameId)
        val date: TextView = itemView.findViewById<TextView>(R.id.dateTextId)
        val viewDetailsButton: MaterialButton = itemView.findViewById(R.id.viewDetailsButton)
        val checkoutButton: MaterialButton = itemView.findViewById(R.id.checkoutButton)
    }

    interface AppointmentItemListener {
        fun onItemClick(setmoreAppointment: SetmoreAppointment)
    }

    interface CheckoutItemListener {
        fun onCheckoutItemClick(setmoreAppointment: SetmoreAppointment)
    }
}