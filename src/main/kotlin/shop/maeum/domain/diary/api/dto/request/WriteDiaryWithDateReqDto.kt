package shop.maeum.domain.diary.api.dto.request

import shop.maeum.domain.diary.domain.PrivacySetting
import java.time.LocalDate

data class WriteDiaryWithDateReqDto(
    val title: String,
    val content: String,
    val privacySetting: PrivacySetting,
    val createdAt: LocalDate
)