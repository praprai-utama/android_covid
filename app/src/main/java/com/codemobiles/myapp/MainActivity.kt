package com.codemobiles.myapp

import android.content.ContextWrapper
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()


        login_button.setOnClickListener {
            val username = username_edittext.text.toString()
            val password = password_edittext.text.toString()

            Prefs.putString("USERNAME", username)
            Prefs.putBoolean("IS_LOGIN", true)

            Toast.makeText(applicationContext, username + password , Toast.LENGTH_SHORT).show()
        }
    }
}



