package com.example.taskapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.taskapp.repository.ListRepository
import com.example.taskapp.util.CurrentState
import com.example.taskapp.utils.TestCoroutineRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@SmallTest
class ListViewModelTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    @Inject
    @Named("Repos")
    lateinit var repository: ListRepository


    @Before
    fun setUp() {
       hiltRule.inject()
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
           val viewModel=ListViewModel(repository)
            viewModel.setStateEvent(MainStateEvent.ListEvent)
            viewModel.getDataState.observeForever {

                when(it)
                {
                    is CurrentState.Success ->
                    {
                        assert(true)
                    }
                    is CurrentState.Error ->
                    {
                        assert(false)
                    }
                }
            }
        }
    }
    @Test
    fun givenError_whenFetch_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            val viewModel = ListViewModel(repository)
            viewModel.setStateEvent(MainStateEvent.None)
            viewModel.getDataState.observeForever {_dataState->
                when(_dataState) {
                    is CurrentState.Error -> {
                        assert(true)
                    }
                    is CurrentState.Success->
                    {
                        assert(false)
                    }
                }
            }
        }
    }
}