package com.styledbylovee.styledemployee

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.styledbylovee.styledemployee.data.appointment.AppointmentRecyclerViewAdapter
import com.styledbylovee.styledemployee.data.appointment.SetmoreAppointment
import com.styledbylovee.styledemployee.data.product.Product
import com.styledbylovee.styledemployee.data.product.ProductViewModel
import com.styledbylovee.styledemployee.util.LOG_TAG
import com.styledbylovee.styledemployee.util.MyLifeCycleObserver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AppointmentRecyclerViewAdapter.CheckoutItemListener{

    private lateinit var productViewModel: ProductViewModel

    private lateinit var navView: BottomNavigationView

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)


        navView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)

//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_appointment, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )

        productViewModel =
            ViewModelProviders.of(this).get(ProductViewModel::class.java)

//        productViewModel.createTransactionNumber()


        productViewModel.transactionNumberData.observe(this, Observer {
            Log.i(LOG_TAG, "Transaction: $it")
            val dest = navController.currentDestination

            Log.i(LOG_TAG, "Dest id: ${dest?.id}")
            Log.i(LOG_TAG, "Dest: $dest")


            if (it != null) {
                navView.visibility = View.INVISIBLE
                extended_fab.show()
            }else {
                navView.visibility = View.VISIBLE
                extended_fab.hide()
            }
        })


//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_log_out -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                Toast.makeText(this, "Logging Out",
                    Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {

        val label = navController.currentDestination?.label

        Log.i(LOG_TAG,"Current Fragment: $label")

        val labelString = label.toString()




        if (labelString != "Appointments" || labelString != "Profile" || labelString != "Payment Display") {
            val newFragment: DialogFragment = TransactionDialogFragment()
            newFragment.show(supportFragmentManager, "Cancel")
        }

    }

//    override fun onCheckoutItemClick(setmoreAppointment: SetmoreAppointment) {
//        CheckoutDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
//    }

    fun checkout(view: View) {
//        CheckoutDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")

    }

//    override fun onCheckoutItemClick(product: Product) {
//        navView.visibility = View.INVISIBLE
//        extended_fab.show()
//    }

    override fun onCheckoutItemClick(setmoreAppointment: SetmoreAppointment) {
        productViewModel.createTransactionNumber()
    }

    fun addItem(view: View) {
        navController.navigate(R.id.checkoutFragment)
    }

    class TransactionDialogFragment : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return requireActivity().let {
                // Use the Builder class for convenient dialog construction
                val builder = AlertDialog.Builder(it)

                builder.setMessage("Cancel Transaction?")
                    .setPositiveButton("Yes",
                        DialogInterface.OnClickListener { dialog, id ->
                            findNavController().navigate(R.id.navigation_appointment)
                        })
                    .setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog
                        })
                // Create the AlertDialog object and return it
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }        }
}