package ru.tinkoff.allure.android.sample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.ActivityCompat

private val REQUEST_ID_READ_PERMISSION = 100
private val REQUEST_ID_WRITE_PERMISSION = 200


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        askPermissionAndReadFile()
        askPermissionAndWriteFile()
    }

    private fun askPermissionAndWriteFile() {
        val canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        //
        if (canWrite) {
          //  this.writeFile()
        }
    }

    private fun askPermissionAndReadFile() {
        val canRead = this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        //
        if (canRead) {
          //  this.readFile()
        }
    }

    // With Android Level >= 23, you have to ask the user
    // for permission with device (For example read/write data on the device).
    private fun askPermission(requestId: Int, permissionName: String): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have permission
            val permission = ActivityCompat.checkSelfPermission(this, permissionName)


            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        arrayOf(permissionName),
                        requestId
                )
                return false
            }
        }
        return true
    }


}
