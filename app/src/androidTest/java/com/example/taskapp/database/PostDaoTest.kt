package com.example.taskapp.database

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.taskapp.datamodel.PostDatabaseEntity
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@SmallTest
class PostDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Inject
    @Named("post_db")
    lateinit var database: PostDatabase
    private lateinit var postDao: PostDao

    @Before
    fun setup() {
        hiltRule.inject()
        postDao = database.postDao()
    }

    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun insertPost() = runBlockingTest {
        val post = PostDatabaseEntity(
            id=1,
            userId=123,
            title = "Hi",
            body = "Hello"
        )
        postDao.insert(post)
        val allPost = postDao.getList()
        assertThat(allPost).contains(post)
    }
    @Test
    fun isListAvailable() = runBlockingTest {
        val list= postDao.getList()
        if(list.isNotEmpty())
        assertThat(true)
        else
            assertThat(false)
    }
    @Test
    fun updatePost()
    {
        runBlockingTest {
            val post=PostDatabaseEntity(id=1,userId = 123,title = "Hello",body = "Hello")
            val success=postDao.updateItem(post)
            if(success>0) {
                assertThat(true)
            }
            else
                assertThat(false)


        }
    }
    @Test
    fun deletePost() {
        runBlockingTest {
            val delete = postDao.deleteItem(PostDatabaseEntity(1, 123, "Hi", "Hello"))
            val allPost=postDao.getList()
            assertThat(allPost).doesNotContain(delete)
        }
    }

}