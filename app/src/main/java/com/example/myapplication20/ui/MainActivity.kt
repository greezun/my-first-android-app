package com.example.myapplication20.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication20.databinding.ActivityMainBinding
import java.util.*
private const val KEY_INTENT = "userData"
private const val FIRST_DELIMITER = "@"
private const val SECOND_DELIMITER = "."
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUserName()
    }

    private fun setUserName() {
        var userName = intent.getStringExtra(KEY_INTENT)
        if (userName != null) {
            userName = parseMail(userName)
        }
        binding.userName.text = userName
    }

    private fun parseMail(mail: String): String {
        val (firstName, secondName) = mail
            .lowercase()
            .split(FIRST_DELIMITER)
            .first()
            .split(SECOND_DELIMITER)
        return "${ firstName.replaceFirstChar { it.titlecase(Locale.getDefault())
        }} ${secondName.replaceFirstChar { it.titlecase(Locale.getDefault()) }}"
    }

}


