package shop.maeum.domain.fcm.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import shop.maeum.domain.fcm.domain.MemberFcmToken

interface MemberFcmTokenRepository : JpaRepository<MemberFcmToken, Long> {

    fun findByToken(fcmToken: String): MemberFcmToken?

    fun findAllByMemberIdAndActiveTrue(memberId: String): List<MemberFcmToken>
}
