package shop.maeum.domain.fcm.domain

import jakarta.persistence.*
import shop.maeum.global.entity.BaseEntity

@Entity
class FcmNotification(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val body: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberFcmToken_id", nullable = false)
    val memberFcmToken: MemberFcmToken

) : BaseEntity() {
}
