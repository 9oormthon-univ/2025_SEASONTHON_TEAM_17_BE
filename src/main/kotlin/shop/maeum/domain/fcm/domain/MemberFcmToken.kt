package shop.maeum.domain.fcm.domain

import jakarta.persistence.*
import shop.maeum.domain.member.entity.Member
import java.time.LocalDateTime

@Entity
class MemberFcmToken(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true, length = 255)
    val token: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @Column(nullable = false)
    var active: Boolean = true,

    val deviceInfo: String? = null,

    var registeredAt: LocalDateTime? = null,

    var expiredAt: LocalDateTime? = null,

    var lastUsedAt: LocalDateTime? = null

) {

    fun deactivate() {
        this.active = false
        this.expiredAt = LocalDateTime.now()
    }

    fun activate() {
        this.active = true
        this.expiredAt = null
        this.lastUsedAt = LocalDateTime.now()
    }
}
