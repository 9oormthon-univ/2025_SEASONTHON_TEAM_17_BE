package shop.maeum.domain.emotion.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import shop.maeum.domain.member.entity.Member
import shop.maeum.global.entity.BaseEntity

@Entity
class EmotionLike(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_id")
    @JsonBackReference
    val emotion: Emotion,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,
) : BaseEntity()
