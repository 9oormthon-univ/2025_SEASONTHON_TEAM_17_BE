package shop.maeum.domain.member.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import shop.maeum.domain.oauth.domain.SocialAccount
import shop.maeum.global.entity.BaseEntity
import shop.maeum.global.entity.Status
import java.time.LocalDateTime

@Entity
@Table(name = "member")
class Member (

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    val id : String?,

    @Column(name = "member_email")
    val email : String,

    @Column(name = "member_nickname")
    var nickname: String,

    @Column(name = "member_profile_path")
    var profilePath: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "member_social_account")
    var socialAccount: SocialAccount?,

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    var memberRole: MemberRole,

    @Column(name = "member_activate", nullable = false)
    var status: Status = Status.ACTIVE,

    @Enumerated(EnumType.STRING)
    @Column(name = "member_privacy")
    var privacy: PrivacySetting,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null

): BaseEntity()
