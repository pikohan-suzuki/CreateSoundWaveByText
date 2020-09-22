package com.amebaownd.pikohan_niwatori.recordsoundandtext

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amebaownd.pikohan_niwatori.recordsoundandtext.data.Constants.REQUEST_CODE
import com.amebaownd.pikohan_niwatori.recordsoundandtext.util.getViewModelFactory

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels { getViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)


        if(Build.VERSION.SDK_INT >= 23) {
            if ((ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED)
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                    ),
                    REQUEST_CODE
                )
                return
            }
        }
        setID()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CODE){
            if(grantResults.isNotEmpty()){
                for(i in permissions.indices){
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        return
                }
                setID()
            }
        }
    }

    private fun setID(){
        val id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        else{
            val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            if(telephonyManager?.deviceId != null) {
                telephonyManager.deviceId
            }else{
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            }
        }
        viewModel.setID(id)
    }
}
