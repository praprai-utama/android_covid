package com.codemobiles.myapp

import android.content.ContextWrapper
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codemobiles.myapp.databinding.ActivityMainBinding
import com.pixplicity.easyprefs.library.Prefs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        setupEventWidget()
    }

    private fun setupEventWidget() {
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEdittext.text.toString()
            val password = binding.passwordEdittext.text.toString()

            Prefs.putString(PREF_USERNAME, username)
            Prefs.putBoolean(PREF_IS_LOGIN, true)

            Toast.makeText(applicationContext, username + password , Toast.LENGTH_SHORT).show()
        }

        binding.usernameEdittext.setText(Prefs.getString(PREF_USERNAME, ""))
    }
}



