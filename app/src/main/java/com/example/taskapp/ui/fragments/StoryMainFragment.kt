package com.example.taskapp.ui.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.taskapp.R
import com.example.taskapp.databinding.StoryViewPagerBinding
import com.example.taskapp.util.transitions.CubeInTransformer

class StoryMainFragment:Fragment() {
    private lateinit var binding:StoryViewPagerBinding
    private lateinit var adapter:ViewPagerAdapter2
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding= StoryViewPagerBinding.inflate(LayoutInflater.from(context),null,false)
        setUpViewPager()
        receiveBroadcast()
        return binding.root
    }

    private fun receiveBroadcast() {
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (intent?.action == "next")
                        binding.storyPager.currentItem = binding.storyPager.currentItem+1
                }

            }, IntentFilter("next"))
        }

    }

    private fun setUpViewPager() {
        adapter=ViewPagerAdapter2()
        val fragment=StoryFragment()
        val list= arrayListOf<Int>()
        list.add(R.drawable.nature1)
        list.add(R.drawable.nature2)
        val bundle=Bundle()
        bundle.putIntegerArrayList("drawables",list)
        fragment.arguments=bundle
        val fragment2=StoryFragment()
        val list2= arrayListOf<Int>()
        list2.add(R.drawable.nature1)
        val bundle2=Bundle()
        bundle2.putIntegerArrayList("drawables",list2)
        fragment2.arguments=bundle2
        adapter.addFragment(fragment)
        adapter.addFragment(fragment2)
        binding.storyPager.setPageTransformer(CubeInTransformer())
        binding.storyPager.adapter=adapter
    }

     inner class ViewPagerAdapter2: FragmentStateAdapter(
        this
    ) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()


        fun addFragment(fragment: Fragment) {
            mFragmentList.add(fragment)
        }

         override fun getItemCount(): Int {
             return mFragmentList.size
         }

         override fun createFragment(position: Int): Fragment {
             return mFragmentList[position]
         }
     }
}