package shop.maeum.domain.member.application

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import shop.maeum.domain.member.api.dto.LoginInfoDto
import shop.maeum.domain.member.entity.Member
import shop.maeum.domain.member.util.NickNameGenerator
import shop.maeum.domain.member.validator.MemberValidator
import shop.maeum.domain.oauth.client.dto.response.KakaoMemberInfoDto

@Service
class MemberFacadeService(
    private val memberService: MemberService,
    private val memberValidator: MemberValidator,
) {

    fun signUpOrLogin(memberInfo: KakaoMemberInfoDto): LoginInfoDto {
        val isNewMember: Boolean = memberValidator.isValidateEmail(memberInfo.kakaoAccount?.email!!)

        val member: Member =
            if (isNewMember) {
                var tempNickname: String
                do {
                    tempNickname = NickNameGenerator.generateNickName()
                } while (!memberValidator.isValidNickName(tempNickname))
                memberService.saveMember(
                    Member.of(
                        email = memberInfo.kakaoAccount.email,
                        nickname = tempNickname,
                        profilePath = memberInfo.kakaoAccount.profile?.profileImageUrl
                    )
                )
            } else {
                memberService.getByEmail(memberInfo.kakaoAccount.email)
            }

        val statusCode = if (isNewMember) HttpStatus.CREATED else HttpStatus.ACCEPTED
        val message = if (isNewMember) "회원가입 성공" else "로그인 성공"

        return LoginInfoDto(
            member = member,
            status = statusCode,
            message = message
        )
    }
}