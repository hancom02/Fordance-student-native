package com.fordance.student.core.domain.model

enum class DanceGenre(val rawValue: String, val displayName: String) {
    MUTIGENRE("mutigenre", "Multi-genre"),
    BALLET("ballet", "Ballet"),
    HIPHOP("hiphop", "Hip-hop"),
    FOLK("folk", "Folk"),
    SEXY("sexy", "Sexy Dance"),
    WACKING("wacking", "Waacking"),
    LOCKING("locking", "Locking"),
    VOGUE("vogue", "Vogue"),
    CONTEMPORARY("contemporary dance", "Contemporary"),
    CHOREOGRAPHY("choreography", "Choreography");

    companion object {
        fun allRawValues(): List<String> = entries.map { it.rawValue }

        fun fromRaw(value: String?): DanceGenre {
            val normalized = value?.trim()?.lowercase()
            return entries.firstOrNull { it.rawValue.equals(normalized, ignoreCase = true) }
                ?: CHOREOGRAPHY
        }
    }
}
