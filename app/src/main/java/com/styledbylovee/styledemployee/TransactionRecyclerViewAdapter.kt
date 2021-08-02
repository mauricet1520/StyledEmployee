package com.styledbylovee.styledemployee

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.styledbylovee.styledemployee.data.product.Product
import com.styledbylovee.styledemployee.util.LOG_TAG
import java.text.SimpleDateFormat
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class TransactionRecyclerViewAdapter(val context: Context,
                                     private val values: List<Product>,
                                     val itemListener: TransactionItemListener,
                                     val checkoutItemListener: CheckoutItemListener
) : RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder>() {
    private lateinit var storage: FirebaseStorage


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view  = inflater.inflate(R.layout.transaction_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val description = item.name
        val imageView = item.item_image_url
        val store = "Store: ${item.store_name}"
        val cost = "Cost: ${item.cost}"

        holder.description.text = description
        holder.store.text = store
        holder.cost.text = cost

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Log.i(LOG_TAG ,"User: ${user.uid}")
            storage = FirebaseStorage.getInstance()
        } else        {
            Log.i(LOG_TAG ,"User is null")
        }

        val storageRef = storage.reference

        val pathReference =
            storageRef.child(item.item_image_url!!)
        Glide.with(context/* context */)
            .load(pathReference)
            .into(holder.image)


//        holder.viewDetailsButton.setOnClickListener {
//            itemListener.onItemClick(item)
//        }
//
//        holder.checkoutButton.setOnClickListener {
//            checkoutItemListener.onCheckoutItemClick(item)
//        }
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

        val cv: CardView = itemView.findViewById(R.id.transaction_card)
        val description: TextView = itemView.findViewById<TextView>(R.id.descriptionId)
        val store: TextView = itemView.findViewById<TextView>(R.id.storeTextId)
        val cost: TextView = itemView.findViewById<TextView>(R.id.costId)
        val image: ImageView = itemView.findViewById(R.id.product_image_id)
        val editButton: MaterialButton = itemView.findViewById(R.id.editButton)
        val removeButton: MaterialButton = itemView.findViewById(R.id.removeButton)
    }

    interface TransactionItemListener {
        fun onItemClick(product: Product)
    }

    interface CheckoutItemListener {
        fun onCheckoutItemClick(product: Product)
    }
}