package shop.maeum.domain.member.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shop.maeum.domain.member.api.dto.reponse.ChangeNicknameResDto
import shop.maeum.domain.member.api.dto.reponse.MyPageInfoDto
import shop.maeum.domain.member.api.dto.request.ChangeNicknameReqDto
import shop.maeum.domain.member.entity.Member
import shop.maeum.domain.member.repository.MemberRepository
import shop.maeum.domain.security.util.SecurityUtil
import shop.maeum.global.entity.Status

@Service
@Transactional(readOnly = true)
class MemberService (
    private val memberRepository: MemberRepository,
    private val securityUtil: SecurityUtil,
){
    fun getByEmail(email: String): Member {
        // TODO: 임시 예외처리 -> 세부 예외처리 필요
        return memberRepository.findByEmail(email)?: throw IllegalArgumentException("Member with email $email not found")
    }

    @Transactional
    fun saveMember(member: Member): Member
    = memberRepository.save(member)

    fun findMyPageInfo(email: String): MyPageInfoDto {
        val member = memberRepository.findByEmail(email)
        require(member?.status != Status.UN_ACTIVE) { "유효하지 않은 회원입니다." }
        return MyPageInfoDto(
            nickname = member!!.nickname,
            profilePath = member.profilePath
        )
    }

    @Transactional
    fun changeNickname(changeNicknameReqDto: ChangeNicknameReqDto): ChangeNicknameResDto {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member not found")
        require(member?.status != Status.UN_ACTIVE) { "유효하지 않은 회원입니다." }
        require(!memberRepository.existsByNickname(changeNicknameReqDto.nickname)) { "이미 사용중인 닉네임입니다." }
        member!!.nickname = changeNicknameReqDto.nickname
        return ChangeNicknameResDto(
            nickname = member.nickname,
        )
    }
}