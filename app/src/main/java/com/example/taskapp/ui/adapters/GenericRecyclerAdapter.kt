package com.example.taskapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.BR
import com.example.taskapp.R
import com.example.taskapp.databinding.ListRecyclerContentBinding
import com.example.taskapp.datamodel.PostModel
import com.example.taskapp.datamodel.SampleModelViewType
import com.example.taskapp.util.ListClickListener
import com.example.taskapp.util.ViewType
import java.util.*
import kotlin.collections.ArrayList

class GenericRecyclerAdapter<E : ViewType<*>>(private val listener: ListClickListener<PostModel>): RecyclerView.Adapter<GenericRecyclerAdapter<E>.MyViewHolder>(),Filterable {
    var tracker: SelectionTracker<String>? = null
    var arrayList:ArrayList<E> = arrayListOf()
    var filteredArrayList:ArrayList<E> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val binding=DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_recycler_content, null, false) as ListRecyclerContentBinding
            return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val type=filteredArrayList[position] as SampleModelViewType
        val model=type.data()
                          holder.bind(filteredArrayList[position])
        tracker?.let {
            holder.setSelected(it.isSelected(model.title))
        }
    }
    fun addModel(model:E){
        filteredArrayList.add(model)
    }
    override fun getItemCount(): Int {
        return filteredArrayList.size
    }
    fun setList(list: ArrayList<E>) {
        this.arrayList.clear()
        this.arrayList.addAll(list)
        filteredArrayList.clear()
        filteredArrayList.addAll(list)
        notifyDataSetChanged()
    }
    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<ViewType<*>> = ArrayList()
            if ( constraint.isEmpty()) {
                filteredList.addAll(arrayList)
            } else {
                val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim { it <= ' ' }
                for (item in arrayList) {
                    val type=item as SampleModelViewType
                    val model = type.data()
                    if (model.title.lowercase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            filteredArrayList.clear()
            filteredArrayList.addAll(results.values as ArrayList<E>)
            notifyDataSetChanged()
        }
    }
    inner class MyViewHolder(private val binding: ListRecyclerContentBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: ViewType<*>){
            binding.setVariable(BR.model, item.data())
            binding.setVariable(BR.position, adapterPosition)
            binding.executePendingBindings()
         binding.listContentCardView.setOnClickListener{
            listener.onListClick(item.data() as PostModel)
            }
        }
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<String> =
                object : ItemDetailsLookup.ItemDetails<String>() {
                    override fun getPosition(): Int = adapterPosition
                    override fun getSelectionKey(): String{
                        val model =filteredArrayList[adapterPosition].data() as PostModel
                    return model.title}
                    }

        fun setSelected(selected: Boolean) {
            binding.setVariable(BR.selected, selected)
            binding.executePendingBindings()
        }
    }

    override fun getFilter(): Filter {
        return exampleFilter
    }

}
