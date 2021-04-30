package com.styledbylovee.styledemployee.ui.appointment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.styledbylovee.styledemployee.CheckoutDialogFragment
import com.styledbylovee.styledemployee.R
import com.styledbylovee.styledemployee.data.appointment.AppointmentRecyclerViewAdapter
import com.styledbylovee.styledemployee.data.appointment.SetmoreAppointment
import com.styledbylovee.styledemployee.util.LOG_TAG

class AppointmentFragment : Fragment(), AppointmentRecyclerViewAdapter.AppointmentItemListener,
AppointmentRecyclerViewAdapter.CheckoutItemListener{

    private lateinit var appointmentViewModel: AppointmentViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appointmentViewModel =
            ViewModelProvider(this).get(AppointmentViewModel::class.java)

        appointmentViewModel.getSetmoreAppointment("rd4d21596292055895")
        val root = inflater.inflate(R.layout.fragment_appointment, container, false)

        recyclerView = root.findViewById(R.id.toolRecyclerViewId)


//        val textView: TextView = root.findViewById(R.id.text_home)
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

        val action =
                AppointmentFragmentDirections.actionNavigationAppointmentToCheckoutFragment()
        findNavController().navigate(action)
    }
}