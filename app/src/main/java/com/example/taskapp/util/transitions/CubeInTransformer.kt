package com.example.taskapp.util.transitions

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class CubeInTransformer:ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.pivotX = (if (position < 0f) page.width.toFloat() else 0f)
        page.pivotY = page.height * 0.5f
        page.rotationY = 90f * position
    }
}