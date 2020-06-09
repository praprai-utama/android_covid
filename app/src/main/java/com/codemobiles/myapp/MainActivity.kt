package com.codemobiles.myapp

import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codemobiles.myapp.databinding.ActivityMainBinding
import com.pixplicity.easyprefs.library.Prefs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        if(Prefs.getBoolean(PREF_IS_LOGIN, false)){
            openHomeActivity()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEventWidget()
    }

    private fun setupEventWidget() {
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEdittext.text.toString()
            val password = binding.passwordEdittext.text.toString()

            Prefs.putString(PREF_USERNAME, username)
            Prefs.putBoolean(PREF_IS_LOGIN, true)

            openHomeActivity()
        }

        binding.usernameEdittext.setText(Prefs.getString(PREF_USERNAME, ""))
    }

    private fun openHomeActivity(){
        val intent = Intent(applicationContext , HomeActivity::class.java)
        startActivity(intent)
    }
}



