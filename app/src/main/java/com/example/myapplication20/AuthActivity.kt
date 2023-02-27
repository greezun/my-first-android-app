package com.example.myapplication20

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle

import android.util.Patterns

import androidx.lifecycle.asLiveData
import com.example.myapplication20.databinding.ActivityAuthBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val KEY_INTENT = "userData"

class AuthActivity : BaseActivity<ActivityAuthBinding>(ActivityAuthBinding::inflate) {

    private lateinit var loginData: LoginData
    private lateinit var eMail: TextInputEditText
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        coroutineScope = CoroutineScope(Job())
        loginData = LoginData(this)
        observeData()
        setListeners()
    }

    override fun setListeners() {
        emailFocusListener()
        passwordFocusListener()
        onClickListener()
    }

    private fun autoLogIn() {
        if (isFieldsValid()) {
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
                putExtra(KEY_INTENT, eMail.text.toString())
            }
    }


    private fun observeData() {
        loginData.autoLoginFlow.asLiveData().observe(this, {
            if (it) {
                loginData.loginFlow.asLiveData().observe(this, {
                    binding.eMailField.setText(it.toString())
                })

                loginData.passwordFlow.asLiveData().observe(this, {
                    binding.passwordField.setText(it.toString())
                })
                autoLogIn()
            }
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
            return getString(R.string.checkEmailError)
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

    @SuppressLint("ResourceType")
    private fun validPassword(): String? {
        val passwordText = binding.passwordField.text.toString()
        if (passwordText.length < getString(R.integer.passwordMinLength).toInt()) {
            return getString(R.string.checkPassword_NumberOfCharacters)
        }
        if (!passwordText.matches(getString(R.string.regex_upper_case).toRegex())) {
            return getString(R.string.checkPassword_UpperCase)
        }
        if (!passwordText.matches(getString(R.string.regex_lower_case).toRegex())) {
            return getString(R.string.checkPassword_LowerCase)
        }
        if (!passwordText.matches(getString(R.string.regex_special_symbol).toRegex())) {
            return getString(R.string.checkPassword_SpecialCharacteers)
        }
        return null
    }

    private fun isFieldsValid(): Boolean {
        binding.eMailContainer.helperText = validEmail()
        binding.passwordContainer.helperText = validPassword()
        val validEmail = binding.eMailContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null
        return validEmail && validPassword
    }

    fun onClickListener() {
        binding.login.setOnClickListener {
            if (isFieldsValid()) {
                intentInit()
                saveLoginData()
                goNextActivity()

            }
        }
    }

    private fun saveLoginData() {
        if (binding.checkBox.isChecked) {
            val login = binding.eMailField.text.toString()
            val password = binding.passwordField.text.toString()
            val isAutoLogin = true

            coroutineScope.launch {
                loginData.storeLoginData(login, password, isAutoLogin)
            }
        }
    }
}