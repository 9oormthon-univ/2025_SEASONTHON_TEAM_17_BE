package shop.maeum.domain.member.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shop.maeum.domain.member.application.MemberService
import shop.maeum.domain.member.dto.reponse.MyPageInfoDto
import shop.maeum.domain.security.util.SecurityUtil
import shop.maeum.global.template.RspTemplate

@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val memberService: MemberService
): MemberDocs {

    @GetMapping("/mypage")
    override fun getMyPageInfo() : RspTemplate<MyPageInfoDto> {
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "마이페이지 정보 조회 성공",
            memberService.findMyPageInfo(SecurityUtil.getCurrentEmail())
        )

    }
}