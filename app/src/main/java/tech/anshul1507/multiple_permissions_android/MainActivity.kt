package tech.anshul1507.multiple_permissions_android

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private var PERMISSION_REQ_CODE = 100

    // List of all permissions
    private var permissions = arrayListOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val listOfPermissionsDenied = arrayListOf<String>()

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
        if (checkAndRequestPermissions()) {
            initApp()
        }

    }

    private fun checkAndRequestPermissions(): Boolean {
        // Check which permissions are not granted
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                listOfPermissionsDenied.add(permission)
            }
        }

        //Ask for non-granted permissions
        if (listOfPermissionsDenied.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listOfPermissionsDenied.toTypedArray(),
                PERMISSION_REQ_CODE
            )
            return false
        }

        // App has all granted permissions
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQ_CODE) {

            // Check there are no denied permissions left
            if (listOfPermissionsDenied.isEmpty()) {
                initApp()
            } else {
                /** If permissions are denied
                 * 1 -> We have option to ask permission ask again, in some cases
                 * 2 -> We have only setting option left which is user manual step
                 */

                for (permission in listOfPermissionsDenied) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        // Case 1
                        showAlertDialog("",
                            "This app needs some permissions to run smoothly",
                            "Accept Permissions",
                            { dialog, _ ->
                                dialog.dismiss()
                                checkAndRequestPermissions()
                            },
                            "Deny Permissions",
                            { dialog, _ ->
                                dialog.dismiss()
                                finish()
                            },
                            false
                        )
                    } else {
                        // Case 2
                        showAlertDialog(
                            "",
                            "Allow Permissions from Settings",
                            "Go to Settings",
                            { dialog, _ ->
                                dialog.dismiss()
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", packageName, null)
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            },
                            "Deny Permissions",
                            { dialog, _ ->
                                dialog.dismiss()
                                finish()
                            },
                            false
                        )
                        break //To end this loop
                    }
                }
            }
        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showAlertDialog(
        title: String,
        msg: String,
        positiveLabel: String,
        positiveOnClick: DialogInterface.OnClickListener,
        negativeLabel: String,
        negativeOnClick: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(positiveLabel, positiveOnClick)
        builder.setNegativeButton(negativeLabel, negativeOnClick)
        builder.setCancelable(isCancelable)

        val alert = builder.create()
        alert.show()
        return alert
    }

    private fun initApp() {
        // app starts
    }
}