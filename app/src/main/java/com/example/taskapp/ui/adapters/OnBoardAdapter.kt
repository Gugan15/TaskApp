package com.example.taskapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.R
import com.example.taskapp.databinding.OnboardScreenContentBinding

class OnBoardAdapter(val context: Context):RecyclerView.Adapter<OnBoardAdapter.OnboardHolder>() {
   inner class OnboardHolder(val binding: OnboardScreenContentBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(pos:Int)
        {
            binding.onboardImage.setImageResource(resources[pos])
            binding.onboardTitle.text = heading[pos]
        }
    }

    private val heading= arrayOf("Browse","Map","Security")
    private val resources= arrayOf(R.drawable.browser,R.drawable.map,R.drawable.security)






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardHolder {
        val inflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding=OnboardScreenContentBinding.inflate(inflater,parent,false)
        return OnboardHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return heading.size
    }
}