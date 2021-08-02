package com.styledbylovee.styledemployee

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.coolreecedev.styledpractice.data.checkout.CheckoutViewModel
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import com.styledbylovee.styledemployee.data.appointment.Appointment
import com.styledbylovee.styledemployee.data.checkout.StripeRequest
import com.styledbylovee.styledemployee.data.customer.Customer
import com.styledbylovee.styledemployee.data.customer.CustomerViewModel
import com.styledbylovee.styledemployee.databinding.FragmentCheckoutPaymentBinding
import com.styledbylovee.styledemployee.ui.appointment.AppointmentViewModel
import com.styledbylovee.styledemployee.util.LOG_TAG
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val APPOINTMENT = "appointment"
private const val CUSTOMER = "customer"
private const val TRANSACTION_NUMBER = "transaction_number"
private const val TOTAL_COST = "total_cost"
/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutPaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckoutPaymentFragment : Fragment() {

    private lateinit var viewModel: CheckoutViewModel
    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var appointmentViewModel: AppointmentViewModel
    private var appointment: Appointment? = null
    private var customer: Customer? = null
    private var transaction_number: String? = null
    private lateinit var stripe: Stripe
    private var total_cost: Long = 0
    private lateinit var binding: FragmentCheckoutPaymentBinding
    private val args: CheckoutPaymentFragmentArgs by navArgs()
    private var customerEmail = ""
    private var currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            appointment = it.getParcelable(APPOINTMENT)
            customer = it.getParcelable(CUSTOMER)
            transaction_number = args.transactionNumber
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(requireActivity()
        ).get(CheckoutViewModel::class.java)

        customerViewModel = ViewModelProvider(requireActivity())
                .get(CustomerViewModel::class.java)

        appointmentViewModel = ViewModelProvider(requireActivity())
                .get(AppointmentViewModel::class.java)

        activity?.extended_fab?.hide()

        stripe = Stripe(requireContext(), "pk_test_08OvXs4KcpfiPblGJxlrTWIl")

        binding = FragmentCheckoutPaymentBinding.inflate(inflater, container, false)

        with(binding.cardInputWidgetId) {
            clearFocus()
        }

        val cost = currencyFormat.format(args.totalCost)
        val price = cost.split(".")
        val priceTogether = "${price[0].substring(1)}${price[1]}"
        Log.i(LOG_TAG, "PriceTogether $priceTogether")

        total_cost = priceTogether.toLong()
        val total = "$$total_cost"
        binding.totalCostId.text = cost
        Log.i(LOG_TAG, "Total Cost $total_cost")
        Log.i(LOG_TAG, "Total Cost $total_cost")

        appointmentViewModel.appointmentData.observe(viewLifecycleOwner, Observer {
            Log.i(LOG_TAG, "Customer id from appointmentData ${it.customer_id}")
            customerViewModel.getCustomerAppointment(it.customer_id!!)
        })

        customerViewModel.customerAppointmentData.observe(viewLifecycleOwner, Observer {
            Log.i(LOG_TAG, "Customer emails from customerAppointmentData ${it.email}")
            customerEmail = it.email ?: ""
        })


        binding.processPaymentButton.setOnClickListener {
            if (customerEmail.isNotEmpty()) {
                Log.i(LOG_TAG, "Processing Payment")

                Log.i(LOG_TAG, "Processing Payment")
                Toast.makeText(requireContext(), "Validating Card", Toast.LENGTH_SHORT).show()
                var cost = total_cost.toLong()
                if (cost > 0) {

                    Log.i(LOG_TAG, "Total $total_cost")
                    Log.i(LOG_TAG, "Cost $cost")

                    Log.i(LOG_TAG, "Cost ${cost}")

                    viewModel.createPaymentIntent(StripeRequest(cost, customerEmail))
                } else {
                    Toast.makeText(requireContext(), "Total Cost is set to $0", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.i(LOG_TAG, "No email")
            }
        }





        viewModel.clientSecret.observe(viewLifecycleOwner, Observer {
            Log.i(LOG_TAG, "Confirming Payment: $it")

            val cardInputWidget = binding.cardInputWidgetId
            cardInputWidget.paymentMethodCreateParams?.let { params ->
                val confirmParams =
                    ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(params, it)
                stripe.confirmPayment(this, confirmParams)
            }
            Log.i(LOG_TAG, "Client: $it")
        })

        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                val status = paymentIntent.status
                if (status == StripeIntent.Status.Succeeded) {
                    Log.i(LOG_TAG, "Payment was Successful")
                    Toast.makeText(requireContext(), "Payment was Successful", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.navigation_appointment)


                } else if (status == StripeIntent.Status.RequiresPaymentMethod) {
                    Toast.makeText(requireContext(), "Payment Failed", Toast.LENGTH_LONG).show()
                    Log.i(LOG_TAG, "Payment Failed w Status: ${status.code}")
                }
            }

            override fun onError(e: Exception) {
                Log.e(LOG_TAG, "Error while processing", e)
                Toast.makeText(requireContext(), "Error Occurred - Try again", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}