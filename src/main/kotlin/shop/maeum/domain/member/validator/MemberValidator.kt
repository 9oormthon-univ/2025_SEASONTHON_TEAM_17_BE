package shop.maeum.domain.member.validator

import org.springframework.stereotype.Component
import shop.maeum.domain.member.repository.MemberRepository

@Component
class MemberValidator(
    private val memberRepository: MemberRepository
) {

    fun isValidNickName(nickname: String): Boolean = !memberRepository.existsByNickname(nickname)

    fun isValidateEmail(email: String): Boolean = !memberRepository.existsByEmail(email)
}