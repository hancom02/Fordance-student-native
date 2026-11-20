package com.fordance.student.core.data.mapper

import com.fordance.student.core.data.dto.ClassDto
import com.fordance.student.core.data.dto.InstructorDto
import com.fordance.student.core.data.dto.SessionDto
import com.fordance.student.core.domain.model.DanceClass
import com.fordance.student.core.domain.model.DanceLevel
import com.fordance.student.core.domain.model.Instructor
import com.fordance.student.core.domain.model.Session

fun SessionDto.toDomain(instructorName: String? = null): Session {
    return Session(
        id = id,
        sessionName = sessionName,
        level = DanceLevel.fromRaw(level),
        genre = genre,
        videoUrl = videoUrl,
        thumbnailUrl = thumbnailUrl.orEmpty(),
        duration = duration.orEmpty(),
        classId = classId,
        price = price ?: 0.0,
        instructorId = instructorId,
        instructorName = instructorName,
        status = status.orEmpty(),
        realDuration = realDuration,
        createdAt = createdAt
    )
}

fun Session.toDto(): SessionDto {
    return SessionDto(
        id = id,
        sessionName = sessionName,
        level = level.rawValue,
        genre = genre,
        videoUrl = videoUrl,
        thumbnailUrl = thumbnailUrl.ifEmpty { null },
        duration = duration.ifEmpty { null },
        classId = classId,
        price = price,
        instructorId = instructorId,
        status = status,
        realDuration = realDuration,
        createdAt = createdAt
    )
}

fun ClassDto.toDomain(): DanceClass {
    return DanceClass(
        id = id,
        className = className,
        whatLearn = whatLearn.orEmpty(),
        whatPrepare = whatPrepare.orEmpty(),
        level = DanceLevel.fromRaw(level),
        genre = genre,
        imageCoverUrl = imageCoverUrl.orEmpty(),
        instructorId = instructorId,
        instructorUsername = instructorUsername.orEmpty(),
        introduce = introduce.orEmpty(),
        sessionCount = sessionCount ?: 0
    )
}

fun DanceClass.toDto(): ClassDto {
    return ClassDto(
        id = id,
        className = className,
        whatLearn = whatLearn,
        whatPrepare = whatPrepare,
        level = level.rawValue,
        genre = genre,
        imageCoverUrl = imageCoverUrl.ifEmpty { null },
        instructorId = instructorId,
        instructorUsername = instructorUsername.ifEmpty { null },
        introduce = introduce.ifEmpty { null },
        sessionCount = sessionCount
    )
}

fun InstructorDto.toDomain(): Instructor {
    return Instructor(
        id = id,
        name = name ?: username.orEmpty(),
        username = username.orEmpty(),
        avatarUrl = avatarUrl.orEmpty(),
        role = role ?: "instructor",
        active = active
    )
}

fun Instructor.toDto(): InstructorDto {
    return InstructorDto(
        id = id,
        name = name,
        username = username,
        avatarUrl = avatarUrl.ifEmpty { null },
        role = role,
        active = active
    )
}

fun com.fordance.student.core.data.dto.SessionDetailDto.toDomain(): com.fordance.student.core.domain.model.SessionDetail {
    return com.fordance.student.core.domain.model.SessionDetail(
        id = id,
        title = sessionName,
        level = DanceLevel.fromRaw(level),
        genre = com.fordance.student.core.domain.model.DanceGenre.fromRaw(genre),
        videoUrl = videoUrl.orEmpty(),
        thumbnailUrl = thumbnailUrl.orEmpty(),
        durationText = duration ?: "45 phút",
        classId = classId,
        price = price ?: 0.0,
        description = description.orEmpty(),
        whatLearn = whatLearn.orEmpty(),
        whatPrepare = whatPrepare.orEmpty(),
        instructor = instructor?.toDomain(),
        danceClass = danceClass?.toDomain(),
        isJoined = isJoined,
        isFavorited = isFavourited
    )
}

fun com.fordance.student.core.data.dto.ClassDetailDto.toDomain(): com.fordance.student.core.domain.model.ClassDetail {
    return com.fordance.student.core.domain.model.ClassDetail(
        id = id,
        className = className,
        level = DanceLevel.fromRaw(level),
        genre = com.fordance.student.core.domain.model.DanceGenre.fromRaw(genre),
        imageCoverUrl = imageCoverUrl.orEmpty(),
        instructor = instructor?.toDomain(),
        introduce = introduce.orEmpty(),
        whatLearn = whatLearn.orEmpty(),
        whatPrepare = whatPrepare.orEmpty(),
        sessions = sessions.map { it.toDomain() },
        totalSessions = totalSessions
    )
}
