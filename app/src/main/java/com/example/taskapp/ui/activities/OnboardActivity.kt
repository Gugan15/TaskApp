package com.example.taskapp.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.taskapp.R
import com.example.taskapp.databinding.OnboardMainBinding
import com.example.taskapp.ui.adapters.OnBoardAdapter
import com.example.taskapp.util.transitions.CubeInTransformer

class OnboardActivity:AppCompatActivity() {
    private lateinit var binding: OnboardMainBinding
    private lateinit var viewPager: ViewPager2
    private var currentPage=0
    private var mDots = arrayListOf<TextView>()
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardMainBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)
        viewPager = binding.viewPager2
        val adapter = OnBoardAdapter(this)
        viewPager.adapter = adapter
        viewPager.setPageTransformer(CubeInTransformer())
        binding.button9.setOnClickListener{
            binding.viewPager2.currentItem=currentPage-1
        }
        binding.button10.setOnClickListener {
            if (binding.viewPager2.currentItem == 2) {
                startActivity(Intent(this, BottomNavigationActivity::class.java))
            } else {
                binding.viewPager2.currentItem = currentPage + 1
            }
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
               setSelected(position)

                currentPage=position
                when(position)
                {
                    0->{binding.button9.visibility= View.INVISIBLE
                    binding.button10.visibility=View.VISIBLE
                    binding.button10.text=resources.getString(R.string.next)
                    setDeselected(1)
                    setDeselected(2)
                }
                    mDots.size-1 -> {
                    setDeselected(0)
                    setDeselected(1)
                    binding.button10.text = resources.getString(R.string.finish)
                }
                else->{
                    setDeselected(0)
                    setDeselected(2)
                    binding.button9.visibility=View.VISIBLE
                    binding.button10.visibility=View.VISIBLE
                    binding.button10.text=resources.getString(R.string.next)
                }
            }


        }})
        addDots()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addDots() {
        for (i in 0..2) {
            val dots = TextView(this)
            dots.text = Html.fromHtml("&#8226;",Html.FROM_HTML_MODE_COMPACT)
            dots.textSize = 35f
            dots.setTextColor(Color.DKGRAY)
            mDots.add(dots)
            binding.linearLayout3.addView(dots)
        }
       setSelected(0)
    }
    private fun setDeselected(position: Int)
    {
        if (mDots.size>0) {
            mDots[position].setTextColor(Color.DKGRAY)
        }
    }
    private fun setSelected(position: Int)
    {
        if (mDots.size>0) {
            mDots[position].setTextColor(Color.LTGRAY)
        }
    }
}
