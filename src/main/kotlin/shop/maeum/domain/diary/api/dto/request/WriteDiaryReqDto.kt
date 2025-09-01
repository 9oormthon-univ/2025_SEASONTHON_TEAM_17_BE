package shop.maeum.domain.diary.api.dto.request
import shop.maeum.domain.diary.domain.PrivacySetting

data class WriteDiaryReqDto(
    val title: String,
    val content: String,
    val privacySetting: PrivacySetting
)
