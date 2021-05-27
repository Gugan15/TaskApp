package com.example.taskapp.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.example.taskapp.R
import com.example.taskapp.databinding.MainContainerBinding
import com.example.taskapp.ui.fragments.LogFragment
import com.example.taskapp.ui.fragments.LoginFragment

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=MainContainerBinding.inflate(LayoutInflater.from(this),null,false)
        setContentView(binding.root)
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.include.toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        checkBiometricAvailable()
        supportFragmentManager.beginTransaction().add(binding.fragmentContainer.id, LogFragment()).commit()

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun checkBiometricAvailable() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                Log.e("MY_APP_TAG","No fingerprints enrolled")
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                Log.e("MY_APP_TAG","Update required")
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                Log.e("MY_APP_TAG","Biometric unsupported")
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                Log.e("MY_APP_TAG","Unknown error occurred")
            }
        }
        authenticateBiometric()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun authenticateBiometric() {
        val executor= this.mainExecutor
        val biometric= executor?.let {
                BiometricPrompt(this, it,object:
                    BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int,
                                                       errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(this@MainActivity,
                            "Authentication error: $errString", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        val frag=supportFragmentManager.findFragmentById(R.id.fragment_container)
                        if (frag != null && frag is LoginFragment) {
                            frag.showNotification()
                        }
                        startActivity(Intent(this@MainActivity,BottomNavigationActivity::class.java))
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(this@MainActivity, "Authentication failed",
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(resources.getString(R.string.biometric_title))
            .setSubtitle(resources.getString(R.string.biometric_sub_title))
            .setNegativeButtonText(resources.getString(R.string.biometric_button))
            .build()
        biometric?.authenticate(promptInfo)
    }
}