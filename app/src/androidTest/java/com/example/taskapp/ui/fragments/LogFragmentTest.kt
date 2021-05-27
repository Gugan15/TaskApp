package com.example.taskapp.ui.fragments

import android.util.Log
import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.taskapp.R
import com.example.taskapp.util.customview.EditTextCustomView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test


class LogFragmentTest{
    private lateinit var scenario: FragmentScenario<LogFragment>
    @Before
    fun setup()
    {
        scenario= launchFragmentInContainer()
    }
    @After
    fun teardown()
    {
    }
  @Test
  fun launchTest()
  {
     onView(withId(R.id.button5)).perform(click())
  }
  @Test
  fun editTextTest()
  {
      onView(withId(R.id.editTextTextPersonName)).perform(setTextAction("gugan.sakthivel10@gmail.com"))
      onView(withId(R.id.editTextTextPersonName2)).perform(setTextAction("gugan.sakthivel10@gmail.com"))
  }
    @Test
    fun validPhoneInputTest_ReturnsTrueIfEmpty()
    {
        onView(withId(R.id.editTextTextPersonName2)).perform(setTextAction("")).check(
            matches(checkText()))
    }
    @Test
    fun validateEmailInputTest_ReturnsTrueIfEmpty(){
        onView(withId(R.id.editTextTextPersonName)).perform(setTextAction("")).check(
            matches(checkText()))
    }
    @Test
    fun validPhoneInputTest_ReturnsTrueIfNotEmpty()
    {
        onView(withId(R.id.editTextTextPersonName2)).perform(setTextAction("8056280016")).check(
            matches(checkTextNotEmpty()))
    }
    @Test
    fun validateEmailInputTest_ReturnsTrueIfNotEmpty(){
        onView(withId(R.id.editTextTextPersonName)).perform(setTextAction("gugan.sakthivel10@gmail.com")).check(
            matches(checkTextNotEmpty()))
    }
    @Test
    fun textClickTest()
    {
        onView(withId(R.id.textView15)).perform(onClick())
    }
    companion object {
        fun checkText():Matcher<View>{
            return object: BoundedMatcher<View,EditTextCustomView>(EditTextCustomView::class.java){
                override fun describeTo(description: Description?) {
                    description?.appendText("Test")
                }

                override fun matchesSafely(item: EditTextCustomView?): Boolean {
                    return item?.editText?.text?.toString()?.isEmpty()!!
                }

            }
        }
        fun checkTextNotEmpty():Matcher<View>{
            return object: BoundedMatcher<View,EditTextCustomView>(EditTextCustomView::class.java){
                override fun describeTo(description: Description?) {
                    description?.appendText("Test")
                }

                override fun matchesSafely(item: EditTextCustomView?): Boolean {
                    return item?.editText?.text?.toString()?.isNotEmpty()!!
                }

            }
        }
        fun setTextAction(text: String?): ViewAction {
            return object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return allOf(
                        isDisplayed(),
                        isAssignableFrom(EditTextCustomView::class.java)
                    )
                }

                override fun getDescription(): String {
                    return "Update"
                }

                override fun perform(uiController: UiController?, view: View?) {
                    return (view as EditTextCustomView).editText.setText(text)
                }

            }
        }

        }
        private fun onClick():ViewAction{
            return object : ViewAction
            {
                override fun getConstraints(): Matcher<View> {
                    return isClickable()
                }

                override fun getDescription(): String {
                    return "Clicked"
                }

                override fun perform(uiController: UiController?, view: View?) {
                    Log.d("CLick","Click Happened")
                }

            }
        }
    }