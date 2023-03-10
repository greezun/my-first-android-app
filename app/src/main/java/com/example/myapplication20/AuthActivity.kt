package com.example.myapplication20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.lifecycle.asLiveData
import com.example.myapplication20.databinding.ActivityAuthBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class AuthActivity : AppCompatActivity() {

    private lateinit var loginData: LoginData
    private lateinit var binding: ActivityAuthBinding
    private lateinit var eMail: TextInputEditText
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        coroutineScope = CoroutineScope(Job())
        loginData = LoginData(this)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        observeData()
        setContentView(binding.root)
        emailFocusListener()
        passwordFocusListener()
        autoLogIn()

    }

    private fun autoLogIn() {

        Log.i("myLog", "start autologin")
        Log.i("myLog", "isValid - ${isValid()}")
        if (isValid()) {
            intentInit()
            goNextActivity()
        }
    }

    private fun goNextActivity() {
        startActivity(intent)
        overridePendingTransition(R.anim.zoom_in, R.anim.static_animation)
    }

    private fun intentInit() {
        eMail = binding.eMailField
        intent = Intent(this, MainActivity::class.java)
            .apply {
                putExtra("key", eMail.text.toString())
            }
    }


    private fun observeData() {
        loginData.loginFlow.asLiveData().observe(this, {
            binding.eMailField.setText(it.toString())
        })

        loginData.passwordFlow.asLiveData().observe(this, {
            binding.passwordField.setText(it.toString())
        })

        loginData.autoLoginFlow.asLiveData().observe(this, {
            if (it) autoLogIn()
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
        intentInit()
        if (isValid()) {
            goNextActivity()
            if (binding.checkBox.isChecked) saveLoginData()
        }
    }

    private fun saveLoginData() {
        val login = binding.eMailField.text.toString()
        val password = binding.passwordField.text.toString()
        val isAutoLogin = true

        coroutineScope.launch {
            loginData.storeLoginData(login, password, isAutoLogin)
        }
    }
}