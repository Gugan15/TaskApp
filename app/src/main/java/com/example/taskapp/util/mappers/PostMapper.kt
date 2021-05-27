package com.example.taskapp.util.mappers

import com.example.taskapp.datamodel.PostEntity
import com.example.taskapp.datamodel.PostModel
import javax.inject.Inject

class PostMapper @Inject constructor():EntityMapper<PostEntity,PostModel> {
    override fun mapFromEntity(entity: PostEntity): PostModel {

        return PostModel(
            userId=entity.userId,
            id = entity.id,
            title = entity.title,
            body = entity.body,
        )
    }

    override fun mapToEntity(domainModel: PostModel): PostEntity {
        return PostEntity(
            userId=domainModel.userId,
            id = domainModel.id,
            title = domainModel.title,
            body = domainModel.body,
        )
    }

    /**
     * Converts the mapped entities to a list of values
     */
    fun mapFromEntityList(entities:List<PostEntity>):List<PostModel>{
        return entities.map { mapFromEntity(it) }
    }
}