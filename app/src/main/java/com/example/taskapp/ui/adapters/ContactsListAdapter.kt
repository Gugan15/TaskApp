package com.example.taskapp.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.example.taskapp.R
import com.example.taskapp.databinding.ContactDetailsFragmentBinding
import com.example.taskapp.datamodel.ContactModel
import com.example.taskapp.util.ListClickListener
import kotlin.random.Random


class ContactsListAdapter(private val context: Context,private val listener:ListClickListener<ContactModel>):RecyclerView.Adapter<ContactsListAdapter.ContactHolder>() {
private var contactList:ArrayList<ContactModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val binding=DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.contact_details_fragment,
            null,
            false
        ) as ContactDetailsFragmentBinding
        return ContactHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
       holder.bind(contactList[position])
    }
    fun setContactList(list: ArrayList<ContactModel>)
    {
        contactList.addAll(list)
    }

    override fun getItemCount(): Int =
        contactList.size
    inner class ContactHolder(val binding: ContactDetailsFragmentBinding):RecyclerView.ViewHolder(
        binding.root
    )
    {
        val colors=arrayOf(Color.BLUE,Color.CYAN,Color.DKGRAY,Color.GRAY,Color.GREEN,Color.MAGENTA,Color.MAGENTA,Color.YELLOW)
        fun bind(item: ContactModel)
        {
            binding.contactName.text=item.name
            val drawable = TextDrawable.builder()
                .buildRound(item.name[0].uppercaseChar().toString(), colors[Random.nextInt(7)])
            binding.imageView4.setImageDrawable(drawable)
            binding.contactLayout.setOnClickListener{
                listener.onListClick(item)
            }
        }

    }

}