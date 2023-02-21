package com.example.myapplication20

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication20.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var userName = intent.getStringExtra("key")
        if (userName != null) {
            userName = parseMail(userName)
        }
        binding.userName.text = userName
    }

    private fun parseMail(mail: String): String {
        val (firstName, secondName) = mail.lowercase().split("@").first().split(".")
        return "${
            firstName.replaceFirstChar { it.titlecase(Locale.getDefault()) }
        } ${secondName.replaceFirstChar { it.titlecase(Locale.getDefault()) }}"
    }

}


