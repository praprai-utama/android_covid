package com.codemobiles.mymap

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.provider.Settings
import android.widget.Toast

fun Context.requestOpenGPS() {
    val provider = Settings.Secure.getString(contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)

    if (!provider.contains("gps")) {
        //if gps is disabled
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        Toast.makeText(this, "Please turn on Location Tracking", Toast.LENGTH_LONG).show()
    }
}

fun Context.isConnectedToInternet(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork = connectivityManager.activeNetworkInfo

    return activeNetwork != null && activeNetwork.isConnected
}


fun Context.showToastLong(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun String.toBath(): String{
    return  "1234" + " bath"
}

// "1234".toBath()   -> "1234 bath"