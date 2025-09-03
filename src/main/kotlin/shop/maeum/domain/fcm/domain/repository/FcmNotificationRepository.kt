package shop.maeum.domain.fcm.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import shop.maeum.domain.fcm.domain.FcmNotification

interface FcmNotificationRepository : JpaRepository<FcmNotification, Long> {

    fun findTop5ByMemberFcmToken_MemberIdOrderByCreatedAtDesc(memberFcmTokenMemberId: String): List<FcmNotification>
}
