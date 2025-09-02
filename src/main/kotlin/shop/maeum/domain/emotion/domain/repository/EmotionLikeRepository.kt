package shop.maeum.domain.diary.repository

import org.springframework.data.jpa.repository.JpaRepository
import shop.maeum.domain.emotion.domain.EmotionLike

interface EmotionLikeRepository : JpaRepository<EmotionLike, Long> {
    fun findByEmotion_Diary_IdAndMember_Id(
        emotion_diary_id: Long, member_id: String
    ): EmotionLike?
}

