package shop.maeum.domain.diary.domain

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import shop.maeum.global.entity.BaseEntity
import shop.maeum.global.entity.Status

@Entity
@SQLDelete(sql = "UPDATE diary SET status = 'UN_ACTIVE' WHERE diary_id = ?")
@Where(clause = "status = 'ACTIVE'")
@EntityListeners(AuditingEntityListener::class)
class Diary (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id", nullable = false)
    val id: Long? = null,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "privacy_setting", nullable = false)
    val privacySetting: PrivacySetting,

    @Column(columnDefinition = "TEXT", nullable = false)
    val feedback: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: Status = Status.ACTIVE,

//    @Column(name = "member_id", nullable = false)
//    val memberId: Long
) : BaseEntity()
