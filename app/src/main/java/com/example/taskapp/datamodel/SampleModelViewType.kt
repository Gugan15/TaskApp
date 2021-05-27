package com.example.taskapp.datamodel

import com.example.taskapp.util.ViewType

data class SampleModelViewType(private val model: PostModel) : ViewType<PostModel> {
    override fun data(): PostModel = model
}