package shop.maeum.domain.fcm.application

import shop.maeum.domain.fcm.domain.repository.MemberFcmTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shop.maeum.domain.fcm.domain.MemberFcmToken
import shop.maeum.domain.member.repository.MemberRepository
import shop.maeum.domain.security.util.SecurityUtil
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class MemberFcmTokenService(
    private val memberFcmTokenRepository: MemberFcmTokenRepository,
    private val memberRepository: MemberRepository,
    private val securityUtil: SecurityUtil
) {

    @Transactional
    fun saveOrActivateFcmToken(fcmToken: String, deviceInfo: String) {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with email ${securityUtil.getCurrentEmail()} not found")
        val tokenOpt = memberFcmTokenRepository.findByToken(fcmToken)

        if (tokenOpt != null) {
            if (tokenOpt.member != member) {
                tokenOpt.deactivate()
            } else {
                tokenOpt.activate()
            }
        } else {
            val newToken = MemberFcmToken(
                id = null,
                token = fcmToken,
                member = member,
                active = true,
                deviceInfo = deviceInfo,
                registeredAt = LocalDateTime.now(),
                expiredAt = null,
                lastUsedAt = null
            )
            memberFcmTokenRepository.save(newToken)
        }
    }

    @Transactional
    fun deactivateFcmToken(fcmToken: String) {
        memberFcmTokenRepository.findByToken(fcmToken)?.deactivate()
    }
}
