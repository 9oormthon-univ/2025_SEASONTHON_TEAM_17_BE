package shop.maeum.domain.member.dto

import org.springframework.http.HttpStatus
import shop.maeum.domain.member.entity.Member

data class LoginInfoDto (
    val member: Member,
    val status: HttpStatus,
    val message: String
)