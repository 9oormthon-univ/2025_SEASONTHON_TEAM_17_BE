package shop.maeum.domain.friend.domain

import jakarta.persistence.*
import shop.maeum.domain.member.entity.Member
import shop.maeum.global.entity.BaseEntity

@Entity
class Friend(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id", nullable = false)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id", nullable = false)
    val fromMember: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id", nullable = false)
    val toMember: Member,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var friendStatus : FriendStatus

) : BaseEntity()
