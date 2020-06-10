package com.codemobiles.myapp

import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codemobiles.myapp.databinding.ActivityMainBinding
import com.codemobiles.myapp.models.Demo
import com.codemobiles.myapp.services.APIClient
import com.codemobiles.myapp.services.APIService
import com.pixplicity.easyprefs.library.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkDemo()

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        if (Prefs.getBoolean(PREF_IS_LOGIN, false)) {
            openHomeActivity()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEventWidget()


    }

    private fun networkDemo() {
        val call: Call<Demo> = APIClient.getClient().create(APIService::class.java).demo()

        Log.d("cm_network", call.request().url().toString())

        // object expression
        call.enqueue(object : Callback<Demo> {
            override fun onFailure(call: Call<Demo>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Demo>, response: Response<Demo>) {
                if (response.isSuccessful) {
                    val listUsers = response.body()
                    val userNumberTwo = listUsers!![1].name
                    Toast.makeText( applicationContext, userNumberTwo ,Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "nok", Toast.LENGTH_SHORT).show()
                }
            }
        })
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

    private fun openHomeActivity() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
    }
}



