package shop.maeum.domain.diary.api.dto.response

import shop.maeum.domain.diary.domain.Diary
import shop.maeum.domain.diary.domain.PrivacySetting
import shop.maeum.domain.emotion.domain.EmotionType

data class WriteDiaryResDto(
    val id: Long? = null,
    val title: String,
    val content: String,
    val privacySetting: PrivacySetting,
    val feedback: String,
    val emotions: List<EmotionType> 
) {
    companion object {
        fun fromEntity(diary: Diary): WriteDiaryResDto {
            return WriteDiaryResDto(
                id = diary.id,
                title = diary.title,
                content = diary.content,
                privacySetting = diary.privacySetting,
                feedback = diary.feedback,
                emotions = diary.emotions.map { it.emotionType }
            )
        }
    }
}
