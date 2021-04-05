package tech.anshul1507.multiple_permissions_android

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private var PERMISSION_REQ_CODE = 100

    // List of all permissions
    var permissions = arrayListOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //for android >= 11
            permissions.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        } else {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        // Check for app permissions
        if(checkAndRequestPermissions()){
            initApp()
        }

    }

    private fun checkAndRequestPermissions(): Boolean{
        return true
    }

    private fun initApp(){
        // app starts
    }
}