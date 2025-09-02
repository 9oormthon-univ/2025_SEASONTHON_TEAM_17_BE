package shop.maeum.domain.diary.repository

import org.springframework.data.jpa.repository.JpaRepository
import shop.maeum.domain.emotion.domain.Emotion

interface EmotionRepository : JpaRepository<Emotion, Long>
