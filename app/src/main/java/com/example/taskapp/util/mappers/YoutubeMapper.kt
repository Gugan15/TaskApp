package com.example.taskapp.util.mappers

import com.example.taskapp.datamodel.*
import javax.inject.Inject

class YoutubeMapper @Inject constructor():EntityMapper<YoutubeEntity, YoutubeModel> {
    override fun mapFromEntity(entity: YoutubeEntity): YoutubeModel {

        return YoutubeModel(
            videoId= entity.videoId.id,
            videoTitle = entity.videoTitle.title,
            description = entity.videoTitle.description,
            image = entity.videoTitle.thumbnails.videoImage.img,
        )
    }



    /**
     * Converts the mapped entities to a list of values
     */
    fun mapFromEntityList(entities:List<YoutubeEntity>):List<YoutubeModel>{
        return entities.map { mapFromEntity(it) }
    }

    override fun mapToEntity(domainModel: YoutubeModel): YoutubeEntity {
        return YoutubeEntity(YoutubeIDModel(domainModel.videoId), YoutubeSnippetModel(domainModel.videoTitle,domainModel.description,
            YoutubeThumbnail(YoutubeThumbnailUrl(domainModel.image)))
        )
    }
}