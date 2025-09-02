package shop.maeum.domain.jwt.dto.response

data class MemberInfoRes(
    val id: String,
    val email: String,
    val role: String,
)
