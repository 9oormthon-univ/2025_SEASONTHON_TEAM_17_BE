package shop.maeum.domain.emotion.application

import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shop.maeum.domain.diary.repository.EmotionLikeRepository
import shop.maeum.domain.diary.repository.EmotionRepository
import shop.maeum.domain.emotion.domain.EmotionLike
import shop.maeum.domain.emotion.exception.EmotionNotFoundException
import shop.maeum.domain.fcm.event.emotion.EmotionLikedEvent
import shop.maeum.domain.member.repository.MemberRepository
import shop.maeum.domain.security.util.SecurityUtil

@Service
@Transactional(readOnly = true)
class EmotionLikeService(
    private val emotionRepository: EmotionRepository,
    private val memberRepository: MemberRepository,
    private val emotionLikeRepository: EmotionLikeRepository,
    private val securityUtil: SecurityUtil,
    private val eventPublisher: ApplicationEventPublisher
) {
    @Transactional
    fun toggleEmotionLike(emotionId: Long) {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member not found")

        val targetEmotion = emotionRepository.findByIdOrNull(emotionId)
            ?: throw EmotionNotFoundException()

        // 가장 먼저 일기 기준으로, 해당 유저가 이미 좋아요한 감정이 있는지 조회
        val diary = targetEmotion.diary
        val likedEmotion = emotionLikeRepository.findByEmotion_Diary_IdAndMember_Id(diary.id!!, member.id!!)

        if (likedEmotion != null) {
            if (likedEmotion.emotion.id == targetEmotion.id) {
                // 이미 같은 감정 좋아요 했으면 → 좋아요 취소
                emotionLikeRepository.delete(likedEmotion)
            } else {
                // 다른 감정 선택했으면 → 기존 거 삭제하고 새로운 거 생성
                emotionLikeRepository.delete(likedEmotion)
                emotionLikeRepository.save(EmotionLike(emotion = targetEmotion, member = member))

                eventPublisher.publishEvent(
                    EmotionLikedEvent(
                        diaryAuthorId = diary.member.id!!,
                        likerNickname = member.nickname,
                        emotionType = targetEmotion.emotionType
                    )
                )
            }
        } else {
            // 좋아요한 적 없으면 → 그냥 추가
            emotionLikeRepository.save(EmotionLike(emotion = targetEmotion, member = member))

            eventPublisher.publishEvent(
                EmotionLikedEvent(
                    diaryAuthorId = diary.member.id!!,
                    likerNickname = member.nickname,
                    emotionType = targetEmotion.emotionType
                )
            )
        }
    }
}
