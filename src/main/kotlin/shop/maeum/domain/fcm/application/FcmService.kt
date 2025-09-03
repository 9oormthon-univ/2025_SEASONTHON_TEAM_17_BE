package shop.maeum.domain.fcm.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.auth.oauth2.GoogleCredentials
import shop.maeum.domain.fcm.domain.repository.MemberFcmTokenRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.http.*
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import shop.maeum.domain.fcm.api.dto.FcmMessageDto
import shop.maeum.domain.fcm.api.dto.FcmSendDto
import shop.maeum.domain.fcm.api.dto.response.FcmNotificationResponses
import shop.maeum.domain.fcm.api.dto.response.FcmSendResponse
import shop.maeum.domain.fcm.domain.FcmNotification
import shop.maeum.domain.fcm.domain.repository.FcmNotificationRepository
import shop.maeum.domain.member.repository.MemberRepository
import shop.maeum.domain.security.util.SecurityUtil
import java.io.IOException
import java.nio.charset.StandardCharsets

@Service
@Transactional(readOnly = true)
class FcmService(
    private val fcmNotificationRepository: FcmNotificationRepository,
    private val memberFcmTokenRepository: MemberFcmTokenRepository,
    private val memberRepository: MemberRepository,
    private val securityUtil: SecurityUtil,
    private val objectMapper: ObjectMapper
) {

    @Value("\${firebase.config-path}")
    lateinit var firebaseConfigPath: String

    private val apiUrl = "https://fcm.googleapis.com/v1/projects/widuy-875a5/messages:send"

    @Transactional
    @Throws(IOException::class)
    fun sendMessageTo(fcmSendDto: FcmSendDto): FcmSendResponse {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with email ${securityUtil.getCurrentEmail()} not found")
        val tokens = memberFcmTokenRepository.findAllByMemberIdAndActiveTrue(member.id!!)
        var successCount = 0

        for (tokenEntity in tokens) {
            val token = tokenEntity.token
            val message = makeMessage(token, fcmSendDto)

            val headers = HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
                setBearerAuth(getAccessToken())
            }

            val entity = HttpEntity(message, headers)
            val restTemplate = RestTemplate().apply {
                messageConverters.add(
                    0,
                    StringHttpMessageConverter(StandardCharsets.UTF_8)
                )
            }

            val response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String::class.java)

            if (response.statusCode == HttpStatus.OK) {
                successCount++
                fcmNotificationRepository.save(
                    FcmNotification(
                        body = fcmSendDto.body,
                        memberFcmToken = tokenEntity
                    )
                )
            }
        }

        return FcmSendResponse.of(fcmSendDto, successCount)
    }

    @Throws(IOException::class)
    private fun getAccessToken(): String {
        val credentials = GoogleCredentials
            .fromStream(ClassPathResource(firebaseConfigPath).inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))

        credentials.refreshIfExpired()
        return credentials.accessToken.tokenValue
    }

    @Throws(IOException::class)
    private fun makeMessage(token: String, dto: FcmSendDto): String {
        val messageDto = FcmMessageDto(
            validateOnly = false,
            message = FcmMessageDto.Message(
                notification = FcmMessageDto.Notification(
                    body = dto.body,
                ),
                token = token
            )
        )
        return objectMapper.writeValueAsString(messageDto)
    }

    fun getNotificationsForCurrentUser(): FcmNotificationResponses {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with email ${securityUtil.getCurrentEmail()} not found")
        val notifications = fcmNotificationRepository.findTop5ByMemberFcmToken_MemberIdOrderByCreatedAtDesc(member.id!!)
        return FcmNotificationResponses.from(notifications)
    }
}
