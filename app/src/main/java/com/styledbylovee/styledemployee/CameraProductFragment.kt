package com.styledbylovee.styledemployee

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.styledbylovee.styledemployee.util.LOG_TAG
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_camera_product.*
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraProductFragment : Fragment() {


    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    lateinit var storage: FirebaseStorage
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    lateinit var user: FirebaseUser
    lateinit var capturePhotoButton: Button
    lateinit var flipCameraButton: Button
    var cameraFacing: Boolean = false
    private var transaction_number: String? = null
    private var sku_number: String? = null
    private val args: CameraFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        storage = FirebaseStorage.getInstance()
        val view = inflater.inflate(R.layout.fragment_camera_product, container, false)

        Log.i(LOG_TAG, "Sku: ${args.skuNumber}")

        transaction_number = args.transactionNumber
        sku_number = args.skuNumber
        if (allPermissionsGranted()) {
            startCamera(false)
        } else {
            ActivityCompat.requestPermissions(
                    activity as MainActivity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        activity?.extended_fab?.hide()

        capturePhotoButton = view.findViewById(R.id.camera_capture_frag_product_button)
        flipCameraButton = view.findViewById(R.id.flipCameraProduct)
        capturePhotoButton.setOnClickListener {
            takePhoto()
        }

        flipCameraButton.setOnClickListener {
            cameraFacing = if (cameraFacing) {
                startCamera(false)
                false
            }else {
                startCamera(true)
                true
            }

        }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
        user = FirebaseAuth.getInstance().currentUser!!

        return view
    }


    private fun startCamera(changeCamera: Boolean) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder()
                    .build()

            imageCapture = ImageCapture.Builder()
                    .build()

            val cameraSelector = if (changeCamera) {
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                        .build()
            }else {
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
            }

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageCapture
                )
                preview?.setSurfaceProvider(viewFinderProduct.surfaceProvider)
            } catch (exc: Exception) {
                Log.e(LOG_TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }


    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case

        Log.i(LOG_TAG, "takePhoto started")

        val imageCapture = imageCapture ?: return

        // Create timestamped output file to hold the image
        val photoFile = File(
                outputDirectory,
                "_$transaction_number" + "SKU_$sku_number" + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Setup image capture listener which is triggered after photo has
        // been taken
        imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {

                        val savedUri = Uri.fromFile(photoFile)

                        if (user != null) {

                            val storageRef = storage.reference


                            val imagesRef: StorageReference? =
                                    storageRef.child("transaction/products/${savedUri.lastPathSegment}")


                            val uploadTask = imagesRef?.putFile(savedUri)
                            uploadTask?.addOnFailureListener {
                                // Handle unsuccessful uploads
                                Log.e(LOG_TAG, "Imaged failed to saved in FB", it)

                            }?.addOnSuccessListener { taskSnapshot ->
                                Log.i(LOG_TAG, "Imaged saved in FB")
                                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                                // ...
                            }
                        } else {
                            Log.i(LOG_TAG, "user is null")
                        }

                        val msg = "Photo capture succeeded: $savedUri"
//                    Toast.makeText(context, "Thank you", Toast.LENGTH_SHORT).show()
//                    Toast.makeText(context, "Sign up completed", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, msg)
//                    val bundle = Bundle()
//                    bundle.putParcelable(APPOINTMENT, appointment)
//                    bundle.putParcelable(CUSTOMER, customer)

//                    bundle.putString(TRANSACTION_NUMBER, transaction_number)
//                    bundle.putString(SKU_NUMBER, sku_number)
//
                    findNavController().navigate(R.id.transactionFragment)
                    }
                })
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
                requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().filesDir
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults:
            IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera(false)
            } else {
                Toast.makeText(
                        context,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}