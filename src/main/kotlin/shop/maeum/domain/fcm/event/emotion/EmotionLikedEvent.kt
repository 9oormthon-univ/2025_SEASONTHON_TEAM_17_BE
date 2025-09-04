package shop.maeum.domain.fcm.event.emotion

import shop.maeum.domain.emotion.domain.EmotionType

data class EmotionLikedEvent(
    val diaryAuthorId: String,
    val likerNickname: String,
    val emotionType: EmotionType
)
