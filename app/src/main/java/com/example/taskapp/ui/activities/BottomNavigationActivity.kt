package com.example.taskapp.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import com.example.taskapp.R
import com.example.taskapp.databinding.BottomTabMainBinding
import com.example.taskapp.ui.fragments.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BottomNavigationActivity:AppCompatActivity() {
    private var prevMenuItem: MenuItem?=null
    private lateinit var adapter:ViewPagerAdapter2
    private lateinit var bottomTabMainBinding:BottomTabMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       bottomTabMainBinding=DataBindingUtil.setContentView(this, R.layout.bottom_tab_main) as BottomTabMainBinding
        setupViewPager(bottomTabMainBinding.bottomTabViewpager)
        bottomTabMainBinding.bottomTabViewpager.currentItem=0
        BubbleShowCaseBuilder(this) //Activity instance
            .title("Navigate with tabs") //Any title for the bubble view
            .targetView(bottomTabMainBinding.bottomNavigationView).showOnce("123").show()//Display the ShowCase
        bottomTabMainBinding.bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId)
            {
                R.id.menu_list -> bottomTabMainBinding.bottomTabViewpager.currentItem = 0
                R.id.menu_map -> bottomTabMainBinding.bottomTabViewpager.currentItem = 1
                R.id.menu_webview -> bottomTabMainBinding.bottomTabViewpager.currentItem = 2
                R.id.menu_player -> bottomTabMainBinding.bottomTabViewpager.currentItem = 3
            }
            true
        }
        bottomTabMainBinding.bottomTabViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                prevMenuItem?.isChecked = false
                bottomTabMainBinding.bottomNavigationView.menu.getItem(position).isChecked = true
                prevMenuItem = bottomTabMainBinding.bottomNavigationView.menu.getItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    private fun setupViewPager(viewPager: ViewPager2) {
        adapter = ViewPagerAdapter2()
        val listFragment=ListFragment()
        val maps=MapsFragment()
        val web=WebViewFragment()
        val player=YoutubeFragment()
        adapter.addFragment(listFragment)
        adapter.addFragment(maps)
        adapter.addFragment(web)
        adapter.addFragment(player)
        viewPager.adapter = adapter
    }
    inner class ViewPagerAdapter2 : FragmentStateAdapter(
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