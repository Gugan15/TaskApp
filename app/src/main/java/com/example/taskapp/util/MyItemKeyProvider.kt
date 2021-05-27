package com.example.taskapp.util

import androidx.recyclerview.selection.ItemKeyProvider
import com.example.taskapp.datamodel.SampleModelViewType
import com.example.taskapp.ui.adapters.GenericRecyclerAdapter

class MyItemKeyProvider(private val adapter: GenericRecyclerAdapter<ViewType<*>>) : ItemKeyProvider<String>(
    SCOPE_MAPPED)
{
    override fun getKey(position: Int): String {
        val type=adapter.arrayList[position] as SampleModelViewType
        val model=type.data()
        return model.title
    }
    override fun getPosition(key: String): Int =
        adapter.arrayList.indexOfFirst {val model=it as SampleModelViewType
            val sampleModel=model.data()
            sampleModel.title== key}
}