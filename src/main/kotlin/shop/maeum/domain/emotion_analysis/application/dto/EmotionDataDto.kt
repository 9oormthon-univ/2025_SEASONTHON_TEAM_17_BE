package shop.maeum.domain.emotion_analysis.application.dto

import shop.maeum.domain.emotion.domain.Emotion
import shop.maeum.domain.emotion.domain.EmotionType

data class EmotionDataDto(
    val emotionId: Long?,
    val emotionType: EmotionType
) {
    companion object {
        fun fromEmotion(emotion: Emotion): EmotionDataDto {
            return EmotionDataDto(
                emotionId = emotion.id,
                emotionType = emotion.emotionType,
            )
        }
    }
}
