package com.example.taskapp.util

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.ui.adapters.GenericRecyclerAdapter

class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<String>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<String>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as GenericRecyclerAdapter<*>.MyViewHolder).getItemDetails()
        }
        return null
    }
}