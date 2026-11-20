package com.fordance.student.core.data.mapper

import com.fordance.student.core.data.dto.ClassDto
import com.fordance.student.core.data.dto.InstructorDto
import com.fordance.student.core.data.dto.SessionDto
import com.fordance.student.core.domain.model.DanceLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ModelMappingTest {

    @Test
    fun testSessionDtoToDomainAndBack() {
        val dto = SessionDto(
            id = 101L,
            sessionName = "HipHop Basics",
            level = "intermediate",
            genre = "hiphop",
            videoUrl = "https://example.com/video.mp4",
            thumbnailUrl = "https://example.com/thumb.jpg",
            duration = "45",
            classId = 5L,
            price = 15.0,
            instructorId = "ins-1",
            status = "approved"
        )

        val domain = dto.toDomain(instructorName = "John Doe")
        assertEquals(101L, domain.id)
        assertEquals("HipHop Basics", domain.sessionName)
        assertEquals(DanceLevel.INTERMEDIATE, domain.level)
        assertEquals("hiphop", domain.genre)
        assertEquals("John Doe", domain.instructorName)
        assertEquals(15.0, domain.price, 0.001)

        val mappedBackDto = domain.toDto()
        assertEquals(dto.id, mappedBackDto.id)
        assertEquals(dto.sessionName, mappedBackDto.sessionName)
        assertEquals(dto.level, mappedBackDto.level)
    }

    @Test
    fun testClassDtoToDomainAndBack() {
        val dto = ClassDto(
            id = 202L,
            className = "Contemporary Masterclass",
            whatLearn = "Fluid movements",
            whatPrepare = "Comfortable clothes",
            level = "advanced",
            genre = "contemporary dance",
            imageCoverUrl = "https://example.com/cover.jpg",
            instructorId = "ins-2",
            instructorUsername = "maria_dance",
            introduce = "Advanced contemporary choreography",
            sessionCount = 8
        )

        val domain = dto.toDomain()
        assertEquals(202L, domain.id)
        assertEquals("Contemporary Masterclass", domain.className)
        assertEquals(DanceLevel.ADVANCED, domain.level)
        assertEquals(8, domain.sessionCount)

        val mappedBackDto = domain.toDto()
        assertEquals(dto.id, mappedBackDto.id)
        assertEquals(dto.className, mappedBackDto.className)
    }

    @Test
    fun testInstructorDtoToDomainAndBack() {
        val dto = InstructorDto(
            id = "ins-3",
            name = "Alex Smith",
            username = "alex_s",
            avatarUrl = "https://example.com/avatar.jpg",
            role = "instructor",
            active = true
        )

        val domain = dto.toDomain()
        assertEquals("ins-3", domain.id)
        assertEquals("Alex Smith", domain.name)
        assertEquals("alex_s", domain.username)

        val mappedBackDto = domain.toDto()
        assertEquals(dto.id, mappedBackDto.id)
        assertEquals(dto.name, mappedBackDto.name)
    }
}
