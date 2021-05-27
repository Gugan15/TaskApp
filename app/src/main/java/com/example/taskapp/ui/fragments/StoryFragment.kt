package com.example.taskapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.taskapp.databinding.StoryViewFragmentBinding


class StoryFragment:Fragment() {
    private lateinit var binding:StoryViewFragmentBinding
    private var progressList=ArrayList<ProgressBar>()
    private var list= arrayListOf<Int>()
    private lateinit var thread1:Thread
    private lateinit var thread:Thread
    private var doRun=true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= StoryViewFragmentBinding.inflate(LayoutInflater.from(context),container,false)

        list= arguments?.getIntegerArrayList("drawables") as ArrayList<Int>
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.linearProgress.weightSum= list.size.toFloat()
        for (i in 0 until list.size) {
            val progress = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
            if (list.size == 1) {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(convertDpToPixel(), 0, 0, 0)
                progress.layoutParams = params
                progressList.add(progress)
            } else {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                params.setMargins(convertDpToPixel(), 0, 0, 0)
                progress.layoutParams = params
                progressList.add(progress)
            }
        }
        addProgress(progressList)
    }


    fun addProgress(progressList:ArrayList<ProgressBar>){
        for (progress in progressList)
        {
            binding.linearProgress.addView(progress)
        }
        thread1= Thread {
            if (doRun) {
                for (j in progressList.indices) {
                    activity?.runOnUiThread { changeDrawable(j) }
                    progressList[j].max = 50
                    progressList[j].progress = 0
                    var i = 0
                    thread = Thread {
                        while (i <= 100&&doRun) {
                            Thread.sleep(100)
                            i += 1
                            progressList[j].progress = i
                        }

                    }
                    thread.start()
                    Thread.sleep(5000)

                }
                context?.let { LocalBroadcastManager.getInstance(it).sendBroadcast(Intent("next")) }
            }
        }
        thread1.start()

    }

    override fun onPause() {
        super.onPause()
        for(i in progressList)
        binding.linearProgress.removeView(i)
        doRun=false
    }

    private fun changeDrawable(i:Int) {
        binding.imageView8.setImageResource(list[i])
    }

    private fun convertDpToPixel(): Int {
        return 10 * (context?.resources
            ?.displayMetrics?.densityDpi?.div(DisplayMetrics.DENSITY_DEFAULT)!!)
    }
}