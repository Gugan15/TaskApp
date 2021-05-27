package com.example.taskapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskapp.databinding.YoutubeFragmentBinding
import com.example.taskapp.datamodel.YoutubeModel
import com.example.taskapp.ui.adapters.YoutubeListAdapter
import com.example.taskapp.ui.viewmodel.CurrentStateEvent
import com.example.taskapp.ui.viewmodel.YoutubeViewModel
import com.example.taskapp.util.CurrentState
import com.example.taskapp.util.ListClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YoutubeFragment:Fragment(),ListClickListener<YoutubeModel> {
    private val youtubeViewModel: YoutubeViewModel by viewModels()
    private lateinit var binding: YoutubeFragmentBinding
    private lateinit var youtubeAdapter: YoutubeListAdapter
    private lateinit var list: List<YoutubeModel>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = YoutubeFragmentBinding.inflate(LayoutInflater.from(context), null, false)
        val appActivity = activity as AppCompatActivity
        appActivity.setSupportActionBar(binding.youtubeToolbar.toolbar)
        appActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.youtubeToolbar.toolbar.setNavigationOnClickListener {
            appActivity.onBackPressed()
        }
        setUpRecycler()


        return binding.root
    }

    private fun setUpRecycler() {


        youtubeAdapter = context?.let { YoutubeListAdapter(it,this) }!!

        binding.youtubeRecycler.adapter=youtubeAdapter
        binding.youtubeRecycler.layoutManager = LinearLayoutManager(context)
        subscribeObservers()
    }

    private fun subscribeObservers() {

        youtubeViewModel.setStateEvent(CurrentStateEvent.ListEvent)
        youtubeViewModel.getDataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is CurrentState.Success<List<YoutubeModel>> -> {
                    list = dataState.data
                    displayProgress(false)
                    loadList(dataState.data)
                }
                is CurrentState.Loading -> {
                    displayProgress(true)
                }
                is CurrentState.Error -> {
                    displayProgress(false)
                    displayError(dataState.exception.toString())
                }

            }
        })
    }

    private fun displayError(toString: String) {
        Log.d("ListFragment", toString)
    }

    private fun loadList(data: List<YoutubeModel>) {
        youtubeAdapter.setList(data as ArrayList<YoutubeModel>)
        val model=list[0]
        Log.d("YoutubeFragment",model.videoTitle+model.videoId+model.description+model.image)
        youtubeAdapter.notifyDataSetChanged()
    }

    private fun displayProgress(b: Boolean) {
        if (b) {
            binding.progressBar3.visibility = View.VISIBLE
        } else {
            binding.progressBar3.visibility = View.GONE
        }
    }

    override fun onListClick(model: YoutubeModel) {
        startActivity(Intent(context,YouPlayerFragment::class.java).putExtra("id",model.videoId))
    }
}
