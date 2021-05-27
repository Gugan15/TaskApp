package com.example.taskapp.util

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import androidx.annotation.RequiresApi
import java.util.*

class SpeechUtil(private val context: Context) {
    private lateinit var textToSpeech: TextToSpeech
    private var initialized=false
    fun initTextToSpeech(){
        textToSpeech= TextToSpeech(context) {
            if (it == TextToSpeech.ERROR) {
                textToSpeech.language = Locale.UK
            }
            if(it==TextToSpeech.SUCCESS)
                initialized=true
        }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun speakText(text:String)
    {
        if(initialized)
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null,"speak")
    }
    fun speakTextPreLollipop(text:String)
    {
        if(initialized)
            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null)
    }
    fun destroyTextToSpeech()
    {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}