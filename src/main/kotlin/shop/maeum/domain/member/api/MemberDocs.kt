package shop.maeum.domain.member.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import shop.maeum.domain.member.dto.reponse.MyPageInfoDto
import shop.maeum.global.template.RspTemplate

@Tag(name = "Member", description = "유저 관련 API")
interface MemberDocs {
    @Operation(
        summary = "마이페이지 정보조회",
        description = "토큰을 기반으로 마이페이지 정보를 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "마이 페이지 조회 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                name = "마이페이지 요청 성공 응답 예시",
                value = """
                {
                    "statusCode": 200,
                    "message": "마이페이지 정보 조회 성공",
                    "data": {
                        "nickname": "지혜로운곰96",
                        "profilePath": "http://k.kakaocdn.net/dn/ShA0R/btsMHexygC5...jpg"
                    }
                }
                """
            )]
        )]
    )
    fun getMyPageInfo(): RspTemplate<MyPageInfoDto>
}