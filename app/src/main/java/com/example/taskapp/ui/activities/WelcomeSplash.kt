package com.example.taskapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.taskapp.R
import com.example.taskapp.databinding.WelcomeSplashBinding

class WelcomeSplash:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView(this, R.layout.welcome_splash) as WelcomeSplashBinding
        startTimeCounter()
    }

    private fun startTimeCounter() {
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                startActivity(Intent(applicationContext, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
                finish()
            }
        }.start()
    }
}