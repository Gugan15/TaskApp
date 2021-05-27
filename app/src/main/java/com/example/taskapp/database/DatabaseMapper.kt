package com.example.taskapp.database

import com.example.taskapp.datamodel.PostDatabaseEntity
import com.example.taskapp.datamodel.PostModel
import com.example.taskapp.util.mappers.EntityMapper
import javax.inject.Inject

class DatabaseMapper
@Inject
constructor(): EntityMapper<PostDatabaseEntity, PostModel> {
    override fun mapFromEntity(entity: PostDatabaseEntity): PostModel {
        return PostModel(
            id=entity.id,
            userId=entity.userId,
            title=entity.title,
            body=entity.body,
            )
    }

    override fun mapToEntity(domainModel: PostModel): PostDatabaseEntity {
        return PostDatabaseEntity(
            id=domainModel.id,
            userId=domainModel.userId,
            title=domainModel.title,
            body=domainModel.body,
        )
    }
    fun mapFromEntityList(entities: List<PostDatabaseEntity>):List<PostModel>
    {
        return entities.map { mapFromEntity(it) }
    }
}