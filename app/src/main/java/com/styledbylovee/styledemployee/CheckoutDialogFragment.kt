package com.styledbylovee.styledemployee

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    CheckoutDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class CheckoutDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_checkout_dialog_list_dialog_item, container, false)

//        val add: TextView = view.findViewById(R.id.addMoreText)
//        val itemDescription: TextView = view.findViewById(R.id.itemDescription)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.findViewById<RecyclerView>(R.id.list)?.layoutManager = LinearLayoutManager(context)
        activity?.findViewById<RecyclerView>(R.id.list)?.adapter = ItemAdapter(20)
    }

     inner class ViewHolder (view: View)
        : RecyclerView.ViewHolder(view) {

//         val itemName: TextView = view.findViewById(R.id.itemName)
//         val itemDescription: TextView = view.findViewById(R.id.itemDescription)
    }

     inner class ItemAdapter(val mItemCount: Int) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            val view  = inflater.inflate(R.layout.fragment_checkout_dialog_list_dialog_item, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//            holder.itemName.text = "position.toString()"
//            holder.itemDescription.text = "position.toString()"
        }

        override fun getItemCount(): Int {
            return mItemCount
        }
    }

    companion object {

        // TODO: Customize parameters
        fun newInstance(itemCount: Int): CheckoutDialogFragment =
                CheckoutDialogFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_ITEM_COUNT, itemCount)
                    }
                }

    }
}