package com.example.taskapp.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.taskapp.R
import com.example.taskapp.databinding.DetailActivityLayoutBinding
import com.example.taskapp.ui.fragments.ContactDetailFragment
import com.example.taskapp.ui.fragments.ListDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=DetailActivityLayoutBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)
        if(intent.extras?.getString("listDetail").equals("listDetail"))
        supportFragmentManager.beginTransaction().add(
            binding.detailActivityContainer.id,
            ListDetailFragment()
        ).commit()
        else
            supportFragmentManager.beginTransaction().add(
                binding.detailActivityContainer.id,
                ContactDetailFragment()
            ).commit()
        }
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        if (event.action == MotionEvent.ACTION_DOWN) {
             val frag=supportFragmentManager.findFragmentById(R.id.detail_activity_container)
            if (frag != null && frag is ListDetailFragment) {
                (frag).hideBottomSheetFromOutSide(event)
            }
        }
        return super.dispatchTouchEvent(event)
    }
    }

