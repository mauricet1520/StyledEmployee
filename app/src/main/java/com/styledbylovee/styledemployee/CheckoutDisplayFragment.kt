package com.styledbylovee.styledemployee

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.styledbylovee.styledemployee.data.product.ProductViewModel
import com.styledbylovee.styledemployee.util.LOG_TAG
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_checkout_display.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.math.round
import kotlin.math.sign

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutDisplayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckoutDisplayFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var productViewModel: ProductViewModel
    private var currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    private var transactionNumber  = ""
    private var totalCost:  Double = 0.0




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_checkout_display, container, false)


        productViewModel =
            ViewModelProvider(requireActivity()).get(ProductViewModel::class.java)

        productViewModel.transactionData.observe(viewLifecycleOwner, Observer {



            val cost = currencyFormat.format(it.totalCost)

            val price = cost.split(".")
            val priceTogether = "${price[0].substring(1)}${price[1]}"
            Log.i(LOG_TAG, "PriceTogether ${priceTogether}")
            totalCost = it.totalCost

            Log.i(LOG_TAG, "Total Cost $totalCost")
            view.totalAmount.text = currencyFormat.format(it.totalCost)
        }
        )

        productViewModel.transactionNumberData.observe(viewLifecycleOwner, Observer {
            transactionNumber = it.toString()
        })

        view.completeTransactionButton.setOnClickListener {
            val action = CheckoutDisplayFragmentDirections
                .actionCheckoutDisplayFragmentToCheckoutPaymentFragment(transactionNumber, totalCost.toFloat())

            findNavController().navigate(action)
        }

//        activity?.extended_fab?.hide()



        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CheckoutDisplayFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CheckoutDisplayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        activity?.extended_fab?.hide()
        super.onResume()
    }
}