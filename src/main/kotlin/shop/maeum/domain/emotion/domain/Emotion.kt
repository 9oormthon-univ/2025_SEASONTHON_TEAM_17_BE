package shop.maeum.domain.emotion.domain

import jakarta.persistence.*
import shop.maeum.domain.diary.domain.Diary
import shop.maeum.global.entity.BaseEntity

@Entity
class Emotion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emotion_id")
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "emotion_type", nullable = false)
    val emotionType: EmotionType,

    @Column(name = "emotion_count", nullable = false)
    var emotionCount: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    val diary: Diary
) : BaseEntity()
