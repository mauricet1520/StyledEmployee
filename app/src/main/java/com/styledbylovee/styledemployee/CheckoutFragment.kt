package com.styledbylovee.styledemployee

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.styledbylovee.styledemployee.data.product.Product
import com.styledbylovee.styledemployee.data.product.ProductViewModel
import com.styledbylovee.styledemployee.data.product.Transaction
import com.styledbylovee.styledemployee.databinding.FragmentCheckoutBinding
import com.styledbylovee.styledemployee.ui.appointment.AppointmentViewModel
import com.styledbylovee.styledemployee.util.LOG_TAG
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat
import java.util.*
class CheckoutFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var transaction: Transaction? = null

    private lateinit var appointmentViewModel: AppointmentViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var transactionNumber: String

    private lateinit var binding: FragmentCheckoutBinding

    private var currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

    private lateinit var itemDescription: String
    private lateinit var skuNumber: String
    private lateinit var store: String

    private var priceAmount: Double = 0.0

    private var totalAmount: Double = 0.0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)


        appointmentViewModel =
                ViewModelProvider(requireActivity()).get(AppointmentViewModel::class.java)

        productViewModel =
                ViewModelProvider(requireActivity()).get(ProductViewModel::class.java)

        with(binding.descriptionEditText) {
            itemDescription = text.toString()
        }

        with(binding.storeEditText) {
            store = text.toString()
        }

        activity?.extended_fab?.hide()


        with(binding.skuEditText) {
            Log.i(LOG_TAG, "Text: $text")
            skuNumber = text.toString()
        }

        with(binding.addItem) {
            setOnClickListener {

                if (transaction != null) {
                    transaction!!.totalCost = totalAmount
                    transaction!!.products!!.add(Product(date = Date().toString(),
                            transaction_number = transactionNumber,
                            cost = priceAmount,
                            store_name = binding.storeEditText.text.toString(),
                            item_type = "",
                            item_image_url = "/transaction/products/_$transactionNumber"+"SKU_${binding.skuEditText.text.toString()}.jpg",
                            sku_image_url = "/transaction/_$transactionNumber"+"SKU_${binding.skuEditText.text.toString()}.jpg",
                            sku_number = binding.skuEditText.text.toString(),
                            setmore_customer_key = "",
                            setmore_service_key = "",
                            setmore_appointment_key = "",
                            setmore_staff_key = "",
                            firebase_stylist_id = "",
                            firebase_customer_id = "",
                            firebase_appointment_id = "",
                            name = binding.descriptionEditText.text.toString()
                    ))
                } else {
                    transaction = Transaction(transactionNumber, totalAmount + priceAmount, mutableListOf(
                            Product(date = Date().toString(),
                                    transaction_number = transactionNumber,
                                    cost = priceAmount,
                                    store_name = binding.storeEditText.text.toString(),
                                    item_type = "",
                                    item_image_url = "/transaction/products/_$transactionNumber" + "SKU_${binding.skuEditText.text.toString()}.jpg",
                                    sku_image_url = "/transaction/_$transactionNumber" + "SKU_${binding.skuEditText.text.toString()}.jpg",
                                    sku_number = binding.skuEditText.text.toString(),
                                    setmore_customer_key = "",
                                    setmore_service_key = "",
                                    setmore_appointment_key = "",
                                    setmore_staff_key = "",
                                    firebase_stylist_id = "",
                                    firebase_customer_id = "",
                                    firebase_appointment_id = "",
                                    name = binding.descriptionEditText.text.toString()
                            )
                    ))
                }
                Log.i(LOG_TAG, "Sku: ${binding.skuEditText.text.toString()}")
                productViewModel.saveProductInTransaction(transaction!!)
                val action = CheckoutFragmentDirections
                    .actionCheckoutFragmentToCameraFragment(transactionNumber,
                            binding.skuEditText.text.toString())
                findNavController().navigate(action)
            }
        }

        with(binding.priceEditText) {
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    try {
                        priceAmount = s.toString().toDouble() / 100
                        binding.actualPriceText.text = currencyFormat.format(priceAmount)
                    } catch (e: NumberFormatException) {
                        binding.actualPriceText.text = ""
                        priceAmount = 0.0
                    }
                }
            })
        }

        productViewModel.transactionNumberData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.i(LOG_TAG, "CheckoutFragment transaction Number: $it")
            transactionNumber = it.toString()
            activity?.extended_fab?.hide()
//            productViewModel.getProductsInTransaction(transactionNumber)
        })

        productViewModel.transactionData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { it ->

            it.products?.forEach { product ->
                totalAmount += product.cost!!
            }
            Log.i(LOG_TAG, "CheckoutFragment totalCostAmount $totalAmount")
        })

        return binding.root
    }
}