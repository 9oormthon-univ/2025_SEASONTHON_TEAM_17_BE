package shop.maeum.domain.emotion_analysis.application.dto

import shop.maeum.domain.emotion.domain.EmotionType

data class ExtractLikeRanksDto (
    val emotionType: EmotionType,
    val likes: Int,
)