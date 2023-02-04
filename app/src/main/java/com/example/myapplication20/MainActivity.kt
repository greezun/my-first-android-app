package com.example.myapplication20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.myapplication20.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var message = intent.getStringExtra("key")
        if (message != null) {
            message = parseMail(message)
        }
        binding.userName.text = message


    }

    private fun parseMail(mail: String): String {
        val (firstName, secondName) = mail.lowercase().split("@").first().split(".")
        return "${firstName.replaceFirstChar { it.titlecase(Locale.getDefault()) }
        } ${secondName.replaceFirstChar { it.titlecase(Locale.getDefault()) }}"
    }

    fun onClickGoMine(view: View){
        val intent = Intent(this, SingUpActivity::class.java )
        startActivity(intent)

    }
}


