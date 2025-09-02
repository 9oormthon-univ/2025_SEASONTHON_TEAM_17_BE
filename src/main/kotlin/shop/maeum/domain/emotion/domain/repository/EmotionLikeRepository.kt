package shop.maeum.domain.diary.repository

import org.springframework.data.jpa.repository.JpaRepository
import shop.maeum.domain.emotion.domain.EmotionLike

interface EmotionLikeRepository : JpaRepository<EmotionLike, Long> {
    fun existsByEmotion_Diary_IdAndMember_Id(diaryId: Long, memberId: String): Boolean

    fun countByEmotion_Id(emotionId: Long): Long
}
