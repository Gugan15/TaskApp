package com.example.taskapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taskapp.R
import com.example.taskapp.databinding.YoutubeListContentBinding
import com.example.taskapp.datamodel.YoutubeModel
import com.example.taskapp.util.ListClickListener

class YoutubeListAdapter(private val context: Context,private val listener: ListClickListener<YoutubeModel>):RecyclerView.Adapter<YoutubeListAdapter.YoutubeHolder>() {
    private var sampleList=ArrayList<YoutubeModel>()
    fun setList(list:ArrayList<YoutubeModel>)
    {
        sampleList.addAll(list)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeHolder {
        val view=DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.youtube_list_content,null,false) as YoutubeListContentBinding
        return YoutubeHolder(view,context)
    }

    override fun onBindViewHolder(holder: YoutubeHolder, position: Int) {
        holder.bind(sampleList[position])
    }

    override fun getItemCount(): Int {
        return sampleList.size
    }
    inner class YoutubeHolder(val binding:YoutubeListContentBinding,val context: Context):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item:YoutubeModel){

            binding.youtubeTitle.text=item.videoTitle
            Glide.with(context).load(item.image).into(binding.youtubeImage)
            binding.youtubeMain.setOnClickListener {
                listener.onListClick(item)
            }
        }
    }

}
