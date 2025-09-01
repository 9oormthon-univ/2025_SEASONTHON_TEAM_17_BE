package shop.maeum.domain.diary.api.dto.response
import shop.maeum.domain.diary.domain.Diary
import shop.maeum.domain.diary.domain.PrivacySetting

data class WriteDiaryResDto(
    val title: String,
    val content: String,
    val privacySetting: PrivacySetting
) {
    companion object {
        fun fromEntity(diary: Diary): WriteDiaryResDto {
            return WriteDiaryResDto(
                title = diary.title,
                content = diary.content,
                privacySetting = diary.privacySetting
            )
        }
    }
}
