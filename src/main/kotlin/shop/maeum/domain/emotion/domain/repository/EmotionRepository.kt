package shop.maeum.domain.diary.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import shop.maeum.domain.emotion.domain.Emotion

interface EmotionRepository : JpaRepository<Emotion, Long> {

    @Query("""
    SELECT SIZE(e.likes)
    FROM Emotion e
    WHERE e.id = :emotionId
    """)
    fun findEmotionWithLikeCount(emotionId: Long?): Int?
}
