package shop.maeum.domain.emotion.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    @JsonBackReference
    val diary: Diary,

    @OneToMany(mappedBy = "emotion", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference
    val likes: MutableList<EmotionLike> = mutableListOf()

) : BaseEntity()
