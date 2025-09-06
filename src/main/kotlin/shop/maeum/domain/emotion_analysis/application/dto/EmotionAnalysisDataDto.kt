package shop.maeum.domain.emotion_analysis.application.dto

import shop.maeum.domain.diary.domain.Diary

data class EmotionAnalysisDataDto(
    val diaryId: Long?,
    val content: String,
    val emotionList: List<EmotionDataDto>
) {
    companion object {
        fun fromDiary(diary: Diary): EmotionAnalysisDataDto {
            return EmotionAnalysisDataDto(
                diaryId = diary.id,
                content = diary.content,
                emotionList = diary.emotions.map { EmotionDataDto.fromEmotion(it) }
            )
        }
    }
}
