package com.example.taskapp.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.taskapp.databinding.YoutubePlayerActivityBinding
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer

class YouPlayerFragment :YouTubeBaseActivity(),YouTubePlayer.OnInitializedListener{
    private var id:String=""
    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        if(!p2)
            p1?.cueVideo(id)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=YoutubePlayerActivityBinding.inflate(LayoutInflater.from(this),null,false)
        setContentView(binding.root)
        id=intent.extras?.getString("id","").toString()
        binding.youtubePlayer.initialize("AIzaSyDIDZwOVBNJhbdpEOR6-T2L7qmjrt3EXo0",this)

    }
}