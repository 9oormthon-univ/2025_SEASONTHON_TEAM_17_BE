package shop.maeum.domain.jwt.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shop.maeum.domain.jwt.component.JwtComponent
import shop.maeum.domain.jwt.dto.TokenPair
import shop.maeum.domain.jwt.dto.response.MemberInfoRes
import shop.maeum.domain.member.entity.MemberRole

@RestController
@RequestMapping("/api/v1/test")
class JwtTestController(
    private val jwtComponent : JwtComponent
) {

    @GetMapping("/token")
    fun getToken(): TokenPair {
        return jwtComponent.createTokenPair(
            id = "5d9c2e63-5b4d-45a4-b1cb-7f4381f6a7ef",
            email = "test@example.com",
            role = MemberRole.Member.value
        )
    }

    @GetMapping("/verify")
    fun verifyToken( @RequestHeader("Authorization") authHeader: String?
    ): MemberInfoRes {
        val token = authHeader?.substringAfter("Bearer ")?.trim()
        val claims: JwtComponent.Claims = jwtComponent.verify(token)
        return MemberInfoRes(
            id = claims.id.replace("\"", ""),
            email = claims.email.replace("\"", ""),
            role = if (claims.roles.isNotEmpty()) claims.roles[0] else ""
        )
    }

}