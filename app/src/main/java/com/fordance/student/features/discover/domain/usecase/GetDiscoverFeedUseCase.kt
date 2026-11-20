package com.fordance.student.features.discover.domain.usecase

import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.core.domain.model.DanceClass
import com.fordance.student.core.domain.model.Session
import com.fordance.student.features.discover.domain.repository.DiscoverRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

data class DiscoverFeed(
    val featuredSessions: List<Session> = emptyList(),
    val joinedSessions: List<Session> = emptyList(),
    val classes: List<DanceClass> = emptyList(),
    val favouriteSessions: List<Session> = emptyList()
)

class GetDiscoverFeedUseCase @Inject constructor(
    private val repository: DiscoverRepository
) {
    suspend operator fun invoke(userId: String): ResultWrapper<DiscoverFeed> = coroutineScope {
        val featuredDeferred = async { repository.getFeaturedSessions() }
        val joinedDeferred = async { repository.getJoinedSessions(userId) }
        val classesDeferred = async { repository.getClasses() }
        val favDeferred = async { repository.getFavouriteSessions(userId) }

        val featuredRes = featuredDeferred.await()
        val joinedRes = joinedDeferred.await()
        val classesRes = classesDeferred.await()
        val favRes = favDeferred.await()

        val featured = (featuredRes as? ResultWrapper.Success)?.data ?: emptyList()
        val joined = (joinedRes as? ResultWrapper.Success)?.data ?: emptyList()
        val classes = (classesRes as? ResultWrapper.Success)?.data ?: emptyList()
        val favs = (favRes as? ResultWrapper.Success)?.data ?: emptyList()

        if (featuredRes is ResultWrapper.Error && classesRes is ResultWrapper.Error) {
            ResultWrapper.Error("Không thể tải bảng tin khám phá.")
        } else {
            ResultWrapper.Success(
                DiscoverFeed(
                    featuredSessions = featured,
                    joinedSessions = joined,
                    classes = classes,
                    favouriteSessions = favs
                )
            )
        }
    }
}
