package com.example.myapplication20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.lifecycle.asLiveData
import com.example.myapplication20.databinding.ActivitySingUpBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SingUpActivity : AppCompatActivity() {

    lateinit var loginData: LoginData
    var login = ""
    var password = ""

    private lateinit var binding: ActivitySingUpBinding

    private lateinit var eMail: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginData = LoginData(this)
        emailFocusListener()
        passwordFocusListener()
        observeData()

    }

    private fun observeData() {

        loginData.loginFlow.asLiveData().observe(this, {
            login = it
            binding.eMailField.setText(it.toString())
        })


        loginData.passwordFlow.asLiveData().observe(this, {
            password = it
            binding.passwordField.setText(it.toString())
        })


    }


    private fun emailFocusListener() {
        binding.eMailField.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.eMailContainer.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.eMailField.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid Email Address"
        }
        return null
    }

    private fun passwordFocusListener() {
        binding.passwordField.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.passwordContainer.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.passwordField.text.toString()
        if (passwordText.length < 8) {
            return "Minimum 8 Character Password"
        }
        if (!passwordText.matches(".*[A-Z].*".toRegex())) {
            return "Must Contain 1 Upper-case Character"
        }
        if (!passwordText.matches(".*[a-z].*".toRegex())) {
            return "Must Contain 1 Lower-case Character"
        }
        if (!passwordText.matches(".*[@#\$%^&+=].*".toRegex())) {
            return "Must Contain 1 Special Character (@#\$%^&+=)"
        }

        return null
    }

    private fun isValid(): Boolean {
        binding.eMailContainer.helperText = validEmail()
        binding.passwordContainer.helperText = validPassword()

        val validEmail = binding.eMailContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null
        return validEmail && validPassword
    }

    fun onClickGoMine(view: View) {
        eMail = findViewById(R.id.eMailField)
        val intent = Intent(this, MainActivity::class.java)
            .apply {
                putExtra("key", eMail.text.toString())
            }

        if (isValid()) {
            startActivity(intent)
            overridePendingTransition(R.anim.zoom_in, R.anim.static_animation)
            if (binding.checkBox.isChecked) saveLoginData()
        }
    }


    private fun saveLoginData() {
        login = binding.eMailField.text.toString()
        password = binding.passwordField.text.toString()

        GlobalScope.launch {
            loginData.storeUser(login, password)
        }
    }
}