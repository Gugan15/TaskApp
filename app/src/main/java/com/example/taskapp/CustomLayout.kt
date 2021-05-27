package com.example.taskapp

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager


class CustomLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):ViewGroup(context,attrs, defStyleAttr) {
   private var deviceWidth:Int

    init {
        val service=(context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
       val display =service.defaultDisplay
       val deviceDisplay = Point()
       display.getSize(deviceDisplay)
       deviceWidth = deviceDisplay.x
   }

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }

    /**
     * @return A set of default layout parameters when given a child with no layout parameters.
     */
    override fun generateDefaultLayoutParams(): LayoutParams? {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    /**
     * @return A set of layout parameters created from attributes passed in XML.
     */
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? {
        return MarginLayoutParams(context, attrs)
    }

    /**
     * Called when [.checkLayoutParams] fails.
     *
     * @return A set of valid layout parameters for this ViewGroup that copies appropriate/valid
     * attributes from the supplied, not-so-good-parameters.
     */
    override fun generateLayoutParams(p: LayoutParams?): LayoutParams? {
        return generateDefaultLayoutParams()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        var curWidth: Int
        var curHeight: Int
        var curLeft: Int
        var curTop: Int
        var maxHeight: Int

        //get the available size of child view

        //get the available size of child view
        val childLeft = this.paddingLeft
        val childTop = this.paddingTop
        val childRight = this.measuredWidth - this.paddingRight
        val childBottom = this.measuredHeight - this.paddingBottom
        val childWidth = childRight - childLeft
        val childHeight = childBottom - childTop
        maxHeight = 0
        curLeft = childLeft
        curTop = childTop

        for (i in 0 until count) {
            val child: View = getChildAt(i)
            if (child.visibility == GONE) return
            //Get the maximum size of the child
            child.measure(
                MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.UNSPECIFIED)
            )
            curWidth = child.measuredWidth
            curHeight = child.measuredHeight
            //wrap is reach to the end
            if (curLeft + curWidth >= childRight) {
                curLeft = childLeft
                curTop += maxHeight
                maxHeight = 0
            }
            //do the layout
            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight)
            //store the max height
            Log.d("CustomLayout",child.javaClass.toString()+"width "+curWidth.toString())
            if (maxHeight < curHeight) maxHeight = curHeight
            curLeft += curWidth
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val count = childCount
        // Measurement will ultimately be computing these values.
        var maxHeight = 0
        var maxWidth = 0
        var childState = 0
        var mLeftWidth = 0
        var rowCount = 0

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        val lp=layoutParams as MarginLayoutParams
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == GONE) continue

            // Measure the child.
            val widthUsed: Int = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin
            val heightUsed: Int = child.getMeasuredWidth() + lp.topMargin + lp.bottomMargin
            measureChildWithMargins(child, widthMeasureSpec,widthUsed, heightMeasureSpec,heightUsed)
            maxWidth += maxWidth.coerceAtLeast(child.measuredWidth)
            mLeftWidth += child.measuredWidth

            if (mLeftWidth / deviceWidth > rowCount) {
                maxHeight += child.measuredHeight
                rowCount++
            } else {
                maxHeight = maxHeight.coerceAtLeast(child.measuredHeight)
            }
            childState = combineMeasuredStates(childState, child.measuredState)
        }

        // Check against our minimum height and width
        maxHeight = maxHeight.coerceAtLeast(suggestedMinimumHeight)
        maxWidth = maxWidth.coerceAtLeast(suggestedMinimumWidth)

        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
            resolveSizeAndState(maxHeight, heightMeasureSpec, childState shl MEASURED_HEIGHT_STATE_SHIFT))
    }
}