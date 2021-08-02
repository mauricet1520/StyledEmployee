package com.styledbylovee.styledemployee

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.styledbylovee.styledemployee.data.appointment.AppointmentRecyclerViewAdapter
import com.styledbylovee.styledemployee.data.product.Product
import com.styledbylovee.styledemployee.data.product.ProductViewModel
import com.styledbylovee.styledemployee.databinding.FragmentTransactionBinding
import com.styledbylovee.styledemployee.ui.appointment.AppointmentViewModel
import com.styledbylovee.styledemployee.util.LOG_TAG
import com.styledbylovee.styledemployee.util.MyLifeCycleObserver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.transaction_card.view.*
import kotlinx.coroutines.NonCancellable.cancel
import java.util.*




class TransactionFragment : Fragment(), TransactionRecyclerViewAdapter.CheckoutItemListener,
TransactionRecyclerViewAdapter.TransactionItemListener{

    private lateinit var binding: FragmentTransactionBinding
    private lateinit var productViewModel: ProductViewModel
    private  var navView: BottomNavigationView? = null
    private var addButton: ExtendedFloatingActionButton? = null
    lateinit var storage: FirebaseStorage
    lateinit var transactionNumber: String
    lateinit var skuNumber: String


    private  var totalCost: Double = 0.0

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.extended_fab?.show()

        arguments?.let {
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_transaction, container, false)

        navView = activity?.findViewById(R.id.nav_view)
        addButton = activity?.findViewById(R.id.extended_fab)


        Log.i(LOG_TAG, "Bottom Nav Shown: ${navView?.isShown}")
        navView?.visibility = View.INVISIBLE
        addButton?.visibility = View.VISIBLE

        addButton?.setOnClickListener {
            this.onPause()
            findNavController().navigate(R.id.checkoutDialogFragment)
        }

        Log.i(LOG_TAG, "Bottom Nav Shown: ${navView?.isShown}")

        productViewModel =
            ViewModelProvider(requireActivity()).get(ProductViewModel::class.java)

        productViewModel.transactionNumberData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                transactionNumber = it.toString()
                Log.i(LOG_TAG, "Transaction Number${it}")
                productViewModel.getProductsInTransaction(it.toString())
            } else {
                Log.i(LOG_TAG, "Creating Transaction")
            }
        }
        )

        recyclerView = view.findViewById(R.id.transaction_recycler_view)

        productViewModel.transactionData.observe(viewLifecycleOwner, Observer {
            Log.i(LOG_TAG, "Getting Transaction Data: ${it.transaction_number}")

            if (it.products != null) {

                val adapter =
                    TransactionRecyclerViewAdapter(
                        requireContext(),
                        it.products!!,
                        this,
                        this
                    )
                recyclerView.adapter = adapter
            }
        })

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Log.i(LOG_TAG ,"User: ${user.uid}")
            storage = FirebaseStorage.getInstance()
        } else        {
            Log.i(LOG_TAG ,"User is null")
        }

        (requireActivity() as AppCompatActivity).run {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setHasOptionsMenu(true)

        return view
    }

    override fun onItemClick(product: Product) {
        TODO("Not yet implemented")
    }

    override fun onCheckoutItemClick(product: Product) {
        TODO("Not yet implemented")
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {

            val newFragment: DialogFragment = TransactionDialogFragment()
            newFragment.show(childFragmentManager, "Cancel")
//            findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

    class TransactionDialogFragment : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return requireActivity().let {
                // Use the Builder class for convenient dialog construction
                val builder = AlertDialog.Builder(it)

                builder.setMessage("Cancel Transaction?")
                    .setPositiveButton("Yes",
                        DialogInterface.OnClickListener { dialog, id ->
                            // FIRE ZE MISSILES!
                            findNavController().navigateUp()
                        })
                    .setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog
                        })
                // Create the AlertDialog object and return it
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }        }
//        override fun onCreateDialog(savedInstanceState: Bundle): Dialog {


    override fun onResume() {
        activity?.extended_fab?.show()
        addButton?.visibility = View.VISIBLE
        super.onResume()
    }

//    override fun onPause() {
//        activity?.extended_fab?.show()
//        addButton?.visibility = View.VISIBLE
//        super.onPause()
//    }


}