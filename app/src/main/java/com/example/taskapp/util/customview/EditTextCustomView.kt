package com.example.taskapp.util.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.example.taskapp.R


class EditTextCustomView @JvmOverloads constructor(
     context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context,attrs, defStyleAttr) {

    private var startDrawableView:ImageView
    var endDrawableView:ImageView
    private var startDrawable: Drawable
    private val localContext=context
    private var endDrawable: Drawable
    var helperText: TextView
     var editText:AppCompatEditText
    init {
       inflate(localContext, R.layout.custom_image_edit,this)
        val arr: TypedArray = localContext.obtainStyledAttributes(
            attrs, R.styleable.EditTextCustomView,
            defStyleAttr, 0
        )

        startDrawable = arr.getDrawable(R.styleable.EditTextCustomView_startIconDrawable)!!
        endDrawable = arr.getDrawable(R.styleable.EditTextCustomView_endIconDrawable)!!
        startDrawableView=findViewById(R.id.custom_start_image_view)
        endDrawableView=findViewById(R.id.custom_end_image_view)
        helperText=findViewById(R.id.custom_helper_text_view)
        helperText.setTextColor(Color.RED)
        startDrawableView.setImageDrawable(startDrawable)
        endDrawableView.setImageDrawable(endDrawable)
        editText=findViewById(R.id.custom_edit_text)
        editText.setTextColor(arr.getColor(R.styleable.EditTextCustomView_customTextColor, Color.WHITE))
        editText.textSize = arr.getFloat(R.styleable.EditTextCustomView_customTextSize,14f)
        editText.hint = arr.getString(R.styleable.EditTextCustomView_customHint)
        editText.inputType=if(arr.getString(R.styleable.EditTextCustomView_customInputType)=="phone") InputType.TYPE_CLASS_PHONE else InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        arr.recycle()
    }

}