package com.styledbylovee.styledemployee.ui.appointment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.styledbylovee.styledemployee.R
import com.styledbylovee.styledemployee.data.appointment.AppointmentRecyclerViewAdapter
import com.styledbylovee.styledemployee.data.appointment.SetmoreAppointment
import com.styledbylovee.styledemployee.data.product.ProductViewModel
import com.styledbylovee.styledemployee.util.LOG_TAG

class AppointmentFragment : Fragment(), AppointmentRecyclerViewAdapter.AppointmentItemListener,
AppointmentRecyclerViewAdapter.CheckoutItemListener{

    private lateinit var appointmentViewModel: AppointmentViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var recyclerView: RecyclerView
    private  var navView: BottomNavigationView? = null
    private var addButton: ExtendedFloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity() as AppCompatActivity).run {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        appointmentViewModel =
            ViewModelProvider(requireActivity()).get(AppointmentViewModel::class.java)

        productViewModel =
                ViewModelProvider(requireActivity()).get(ProductViewModel::class.java)
        val user = FirebaseAuth.getInstance().currentUser

        appointmentViewModel.getAllStaff()

        navView = activity?.findViewById(R.id.nav_view)
        addButton = activity?.findViewById(R.id.extended_fab)

        navView?.visibility = View.VISIBLE
        addButton?.visibility = View.INVISIBLE
         


        val root = inflater.inflate(R.layout.fragment_appointment, container, false)

        recyclerView = root.findViewById(R.id.toolRecyclerViewId)




//        val textView: TextView = root.findViewById(R.id.text_home)

        appointmentViewModel.staffData.observe(viewLifecycleOwner, Observer {staffList ->
            staffList.forEach {
                if(user != null) {
                    if (it.email_id == user.email) {
                        it.key?.let { key -> appointmentViewModel.getSetmoreAppointment(key) }
                    }
                }

            }
        })
        appointmentViewModel.setmoreAppointmentData.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                Log.i(LOG_TAG, "No Data")
            } else {

                val adapter =
                    AppointmentRecyclerViewAdapter(
                        requireContext(),
                        it,
                        this,
                            this
                    )
                recyclerView.adapter = adapter
                it.forEach{ appointment ->
                    run {
                        if (appointment.key != null) {
                            Log.i(LOG_TAG, "Appointment : ${appointment.key}")
                            Log.i(LOG_TAG, "Appointment StartTime: ${appointment.startTime}")

                        }
                    }
                }
            }

//            textView.text = "hello"
        })


        return root
    }

    override fun onItemClick(setmoreAppointment: SetmoreAppointment) {

        val directions = AppointmentFragmentDirections
            .actionNavigationAppointmentToAppointDetailFragment(setmoreAppointment.key!!)
        findNavController().navigate(directions)
    }

    override fun onCheckoutItemClick(setmoreAppointment: SetmoreAppointment) {

        productViewModel.createTransactionNumber()

        Log.i(LOG_TAG, "SetmoreAppointment key ${setmoreAppointment.key}")
        appointmentViewModel.getAppointment(setmoreAppointment.key!!)

        val action =
                AppointmentFragmentDirections.actionNavigationAppointmentToTransactionFragment()
        findNavController().navigate(action)
    }


}