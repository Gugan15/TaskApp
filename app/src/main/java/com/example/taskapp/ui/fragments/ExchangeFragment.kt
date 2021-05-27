package com.example.taskapp.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.taskapp.databinding.ExchangeFragmentBinding
import java.text.DecimalFormat


class ExchangeFragment:Fragment(),AdapterView.OnItemSelectedListener
{
    private lateinit var spinner: Spinner
    private var currency:Float=19.95f
    private var list= arrayOf("AED","QAR","SAR")
    private lateinit var binding: ExchangeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=ExchangeFragmentBinding.inflate(LayoutInflater.from(context),null,false)
        spinner=binding.spinner
        spinner.onItemSelectedListener=this
        val aa: ArrayAdapter<String>? =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, list) }
        aa?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = aa
        binding.baseCurrencyEdit.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count!=0&&s?.length!!>0) {
                    if (binding.baseCurrencyEdit.tag == null) {
                        binding.baseCurrencyEdit.removeTextChangedListener(this)
                        val s1=s.toString().replace(",","")
                        convertToRupees(s1.toFloat())
                        binding.baseCurrencyEdit.setText(formatDollar(s1.toFloat()))
                        var newPos=1
                        if(s.length>4)
                            newPos=s.length-4
                        binding.baseCurrencyEdit.setSelection(newPos)
                        binding.baseCurrencyEdit.addTextChangedListener(this)

                    }
                }
                else{
                    binding.baseCurrencyEdit.setSelection(start)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.indianCurrencyEdit.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if( count!=0&&s?.length!! >0) {
                    if(binding.indianCurrencyEdit.tag==null) {

                        binding.indianCurrencyEdit.removeTextChangedListener(this)
                        val s1=s.toString().replace(",","")
                        convertToDollars(s1.toFloat())
                        binding.indianCurrencyEdit.setText(formatRupee(s1.toFloat()))
                        binding.indianCurrencyEdit.addTextChangedListener(this)
                        var newPos=1
                        if(s.length>4)
                            newPos=s.length-4
                        binding.indianCurrencyEdit.setSelection(newPos)
                    }
                }
                else{
                    binding.indianCurrencyEdit.setSelection(start)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected = list[position]
        val s1 = binding.indianCurrencyEdit.text.toString().replace(",", "")
        val s2 = binding.baseCurrencyEdit.text.toString().replace(",", "")
        when (selected) {
            "AED" -> {
                currency = 19.93f
                if (s1.isNotEmpty() && s2.isNotEmpty()) {
                    if(binding.baseCurrencyEdit.tag==null)
                        convertToDollars(s1.toFloat())
                    else if(binding.indianCurrencyEdit.tag==null)
                        convertToRupees(s2.toFloat())
                }
            }
            "QAR" -> {
                currency = 20.11f
                if (s1.isNotEmpty() && s2.isNotEmpty()) {

                    if(binding.baseCurrencyEdit.tag==null)
                        convertToDollars(s1.toFloat())
                    else if(binding.indianCurrencyEdit.tag==null)
                        convertToRupees(s2.toFloat())

                }
            }
            "SAR" -> {
                currency = 19.52f
                if (s1.isNotEmpty() && s2.isNotEmpty()) {
                    if(binding.baseCurrencyEdit.tag==null)
                        convertToDollars(s1.toFloat())
                    else if(binding.indianCurrencyEdit.tag==null)
                        convertToRupees(s2.toFloat())
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun convertToRupees(dollars: Float) {

        val value=dollars*currency
        binding.indianCurrencyEdit.tag = "Tag"
        binding.indianCurrencyEdit.setText(formatRupee(value))
        binding.indianCurrencyEdit.tag=null
    }
    private fun formatRupee(str1:Float):String {
        val str=String.format("%.3f",str1)
        val strFirst=str.split(".")[0]
        val str2="."+str.split(".")[1]
        val formatter= DecimalFormat("##,##,###")
        val add=formatter.format(strFirst.toFloat())
        return add+str2
    }
    private fun formatDollar(str1:Float):String{
        val str=String.format("%.3f",str1)
        val strFirst=str.split(".")[0]
        val str2="."+str.split(".")[1]
        val formatter= DecimalFormat("###,###,###")
        val add=formatter.format(strFirst.toFloat())
        return add+str2
    }
    private fun convertToDollars(rupee: Float) {
        val value= rupee /currency
        binding.baseCurrencyEdit.tag = "Tag"
        binding.baseCurrencyEdit.setText(formatDollar(value))
        binding.baseCurrencyEdit.tag=null
    }
}