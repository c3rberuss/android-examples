package xyz.c3rberus.gallery

import android.Manifest
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import xyz.c3rberus.gallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var selectPhotoFromGalleryContract: ActivityResultLauncher<Array<String>>? = null
    private var requestPermissionContract: ActivityResultLauncher<String>? = null

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    //mime types of files.
    private val filesMimeTypes = arrayOf("image/*")
    private val readExternalStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inflate the view
        _binding = ActivityMainBinding.inflate(layoutInflater)

        //Set view
        setContentView(binding.root)

        //Initialize the contract to Request read External storage permission
        requestPermissionContract =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    selectPhotoFromGalleryContract?.launch(filesMimeTypes)
                }
            }

        //Initialize the contract to open file explorer to choose a image
        selectPhotoFromGalleryContract =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { photoUri ->

                //If image uri is not null set image on ImageView
                if (photoUri != null) {
                    binding.photo.setImageURI(photoUri)
                }
            }


        //Set onClick event
        with(binding) {
            btnOpenGallery.setOnClickListener {
                //Request read external permission
                requestPermissionContract?.launch(readExternalStoragePermission)
            }
        }

    }

    override fun onDestroy() {

        //Prevent memory leaks
        _binding = null
        selectPhotoFromGalleryContract = null
        requestPermissionContract = null
        super.onDestroy()
    }
}