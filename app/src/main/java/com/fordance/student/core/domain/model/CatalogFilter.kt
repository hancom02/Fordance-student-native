package com.fordance.student.core.domain.model

data class CatalogFilter(
    val levels: Set<DanceLevel> = emptySet(),
    val genres: Set<String> = emptySet(),
    val selectedInstructorIds: Set<String> = emptySet()
) {
    val isEmpty: Boolean
        get() = levels.isEmpty() && genres.isEmpty() && selectedInstructorIds.isEmpty()

    fun toggleLevel(level: DanceLevel): CatalogFilter {
        val newLevels = if (levels.contains(level)) levels - level else levels + level
        return copy(levels = newLevels)
    }

    fun toggleGenre(genre: String): CatalogFilter {
        val newGenres = if (genres.contains(genre)) genres - genre else genres + genre
        return copy(genres = newGenres)
    }

    fun toggleInstructor(instructorId: String): CatalogFilter {
        val newInstructors = if (selectedInstructorIds.contains(instructorId)) {
            selectedInstructorIds - instructorId
        } else {
            selectedInstructorIds + instructorId
        }
        return copy(selectedInstructorIds = newInstructors)
    }

    fun reset(): CatalogFilter = CatalogFilter()
}
