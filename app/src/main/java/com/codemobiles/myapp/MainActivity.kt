package com.codemobiles.myapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_button.setOnClickListener {
            var username = username_edittext.text.toString()
            var password = password_edittext.text.toString()

            Toast.makeText(applicationContext, username + password , Toast.LENGTH_SHORT).show()
        }
    }
}



