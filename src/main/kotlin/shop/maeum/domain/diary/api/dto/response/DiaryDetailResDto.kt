package shop.maeum.domain.diary.api.dto.response

import shop.maeum.domain.diary.domain.Diary
import shop.maeum.domain.emotion.domain.EmotionType

data class DiaryDetailResDto(
    val diaryId: Long,
    val title: String,
    val content: String,
    val privacySetting: String,
    val feedbackTitle: String,
    val feedbackContent: String,
    val emotions: List<EmotionCountDto>
) {
    companion object {
        fun fromEntity(diary: Diary): DiaryDetailResDto {
            val emotionCounts = diary.emotions.map { emotion ->
                EmotionCountDto(
                    emotionId = emotion.id!!,
                    type = emotion.emotionType,
                    likeCount = emotion.likes.size
                )
            }

            return DiaryDetailResDto(
                diaryId = diary.id!!,
                title = diary.title,
                content = diary.content,
                privacySetting = diary.privacySetting.name,
                feedbackTitle = diary.feedbackTitle,
                feedbackContent = diary.feedbackContent,
                emotions = emotionCounts
            )
        }
    }

    data class EmotionCountDto(
        val emotionId: Long,
        val type: EmotionType,
        val likeCount: Int
    )
}
