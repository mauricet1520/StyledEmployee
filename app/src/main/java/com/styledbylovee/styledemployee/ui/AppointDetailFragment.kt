package com.styledbylovee.styledemployee.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.styledbylovee.styledemployee.ui.AppointDetailFragmentArgs
import com.styledbylovee.styledemployee.data.customer.CustomerViewModel
import com.styledbylovee.styledemployee.databinding.AppointDetailFragmentBinding
import com.styledbylovee.styledemployee.util.LOG_TAG

class AppointDetailFragment : Fragment() {
    private val args: AppointDetailFragmentArgs by navArgs()
    private lateinit var binding: AppointDetailFragmentBinding
    private lateinit var customerId: String
    lateinit var storage: FirebaseStorage

    companion object {
        fun newInstance() = AppointDetailFragment()
    }

    private lateinit var viewModel: AppointDetailViewModel
    private lateinit var customerViewModel: CustomerViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = AppointDetailFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(AppointDetailViewModel::class.java)

        customerViewModel = ViewModelProvider(this).get(CustomerViewModel::class.java)
        Log.i(LOG_TAG, "Appointment Id ${args.appointmentId}")
               viewModel.getAppointment(args.appointmentId)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Log.i(LOG_TAG ,"User: ${user.uid}")
            storage = FirebaseStorage.getInstance()
        } else        {
            Log.i(LOG_TAG ,"User is null")
        }



        viewModel.appointmentData.observe(viewLifecycleOwner, Observer {

            customerId = it.customer_id!!
            customerViewModel.getCustomerAppointment(it.customer_id!!)



            with(binding.profileAddress) {
                text = it.street_address
            }

            with(binding.profileBudget) {
                text = it.budget
            }

            with(binding.profileZip) {
                text = it.zip
            }

            with(binding.profileCity) {
                text = it.city
            }

            with(binding.profileState) {
                text = it.state
            }



            with(binding.profileEvent) {
                text = it.occasion
            }
            Log.i(LOG_TAG, "Hello")
        })

        customerViewModel.customerAppointmentData.observe(viewLifecycleOwner, Observer {

            with(binding.profileFirstName) {
                text = it.first_name
            }

            with(binding.profileLastName) {
                text = it.last_name
            }

            with(binding.profileSize) {
                if (it.clothing_type == "Men's") {
                    val size = "Blazer Size${it.blazer_size}\n" +
                            "Shirt Size: ${it.shirt_size}\n" +
                            "Pants Size length: ${it.pants_size}\n" +
                            "Pants Size waist: ${it.pants_size}\n" +
                            "Dress Shirt Size: ${it.dress_shirt_size}"
                    text = size
                }else {
                    val size = "Dress Size${it.dress_size}\n" +
                            "Bottom Size: ${it.bottom_size}\n" +
                            "Pant Size length: ${it.pants_size}\n" +
                            "Jumper Size: ${it.jumper_romper_size}\n" +
                            "Top Size: ${it.top_size}"
                    text = size
                }
            }

            with(binding.profileBodyType) {
                val bodyType = StringBuilder()
                it.body_type!!.forEach {
                    bodyType.append("$it \n")
                }
                text = bodyType
            }

            with(binding.profilePhoneNumber) {
                text = it.phone
            }

            with(binding.profileColors) {
                val colors = StringBuilder()
                it.colors!!.forEach {
                    colors.append("$it \n")
                }
                text = colors            }

        with(binding.profilePatterns) {
            val prints = StringBuilder()
            it.print_patterns!!.forEach {
                prints.append("$it \n")
            }
            text = prints
        }

            with(binding.profileEmail) {
                text = it.email
            }

            with(binding.profileImage) {
                val storageRef = storage.reference

                val pathReference =
                        storageRef.child("customer/images/${it.uid}.jpg")

                Glide.with(requireContext()/* context */)
                        .load(pathReference)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(this)
            }





        })





        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AppointDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}