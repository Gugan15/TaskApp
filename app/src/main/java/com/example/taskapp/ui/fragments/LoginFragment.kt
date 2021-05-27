package com.example.taskapp.ui.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.taskapp.R
import com.example.taskapp.databinding.ActivityMainBinding
import com.example.taskapp.ui.activities.BottomNavigationActivity
import com.google.android.material.snackbar.Snackbar

class LoginFragment: Fragment() {
    var emailPattern =Regex( "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    var phonePattern=Regex("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$")
    private var formEmailValid=false
    private val channelId:String="389"
    private val name:String="Push Notifications"
    private val channelDescription:String="Show app notifications"
    private var formPhoneValid=false
    private lateinit var mainBinding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainBinding= ActivityMainBinding.inflate(LayoutInflater.from(context),null,false)
        mainBinding.button.isEnabled=false
        loadPreferences()

        mainBinding.signUpClick.setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container,SignUpFragment()).addToBackStack(null).commit()
        }
        mainBinding.button.setOnClickListener{
            val preferences=context?.getSharedPreferences("my_pref",Context.MODE_PRIVATE)?.edit()
            preferences?.putBoolean("checked",mainBinding.checkBox.isChecked)
            preferences?.apply()
            if(validateCreds()) {
                showNotification()
                startActivity(Intent(context, BottomNavigationActivity::class.java))
            }
            else
                Snackbar.make(mainBinding.root,"Login Failed",Snackbar.LENGTH_LONG).show()
        }
        mainBinding.emailEditTextLayout.setOnFocusChangeListener{_,t->
            if(t){
                if(!formEmailValid)
                mainBinding.emailEditTextLayout.boxStrokeColor=Color.RED
            }
        }
        mainBinding.emailEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (!s.matches(emailPattern)&&s.toString().isNotEmpty()) {
                        mainBinding.emailEditText.error="Invalid Email"
                        mainBinding.emailEditTextLayout.helperText="Please enter valid email id"
                        mainBinding.emailEditTextLayout.boxStrokeColor=Color.RED
                        formEmailValid = false
                    } else {
                        formEmailValid = true
                        mainBinding.emailEditTextLayout.helperText=""
                        mainBinding.emailEditTextLayout.boxStrokeColor= context?.let { ContextCompat.getColor(it,R.color.purple_500) }!!
                    }

                }
            }

            override fun afterTextChanged(s: Editable?) {
                mainBinding.button.isEnabled = formEmailValid && formPhoneValid && s.toString().isNotEmpty()
            }
        })
        mainBinding.phoneEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (!s.matches(phonePattern)&& s.toString().isNotEmpty()) {
                        mainBinding.phoneEditText.error = "Invalid Number"
                        mainBinding.phoneEditTextLayout.helperText="Please enter valid phone number"
                        mainBinding.phoneEditTextLayout.boxStrokeColor=Color.RED
                        formPhoneValid = false
                    } else {
                        formPhoneValid = true
                        mainBinding.phoneEditTextLayout.helperText=""
                        mainBinding.phoneEditTextLayout.boxStrokeColor= context?.let { ContextCompat.getColor(it,R.color.purple_500) }!!
                    }

                }
            }

            override fun afterTextChanged(s: Editable?) {
                mainBinding.button.isEnabled = formEmailValid && formPhoneValid && s.toString().isNotEmpty()
            }
        })


        return mainBinding.root
    }


    fun showNotification() {
        val intent2 = Intent()
        intent2.action = Intent.ACTION_VIEW
        intent2.data = Uri.parse("https://www.google.com/")
        val intent= Intent(context,BottomNavigationActivity::class.java)
        // Making pendingIntent2 to open the GFG contribution
        // page after clicking the actionButton of the notification
        val resultPendingIntent: PendingIntent? = context?.let {
            TaskStackBuilder.create(it).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(intent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        }
        val pendingIntent2 = PendingIntent.getActivity(context, 6, intent2, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationChannel()
        val nBuilder = context?.let {
            NotificationCompat.Builder(it,channelId)
                .setContentTitle("Login")
                .setContentText("Login Successful")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .addAction(0,"LET'S OPEN BROWSER",pendingIntent2)
                .build()
        }
        val nManager = NotificationManagerCompat.from(requireContext())
        if (nBuilder != null) {
            nManager.notify(1, nBuilder)
        }
    }
    private fun notificationChannel() {
        // check if the version is equal or greater
        // than android oreo version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // creating notification channel and setting
            // the description of the channel
            val channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = channelDescription
            }
            // registering the channel to the System
            val notificationManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun validateCreds():Boolean {
        val preferences=context?.getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        return mainBinding.emailEditText.text.toString() == preferences?.getString("email_key","") &&
                mainBinding.phoneEditText.text.toString() == preferences.getString("phone_key","")
    }

  private fun loadPreferences() {
        val preferences=context?.getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        if (preferences != null) {

            if(preferences.getBoolean("checked",false)) {
                mainBinding.checkBox.isChecked=true
                mainBinding.emailEditText.setText(preferences.getString("email_key",""))
                mainBinding.phoneEditText.setText(preferences.getString("phone_key",""))
                formEmailValid=true
                formPhoneValid=true
                mainBinding.button.isEnabled=true
            }
        }

    }
}