package shop.maeum.domain.friend.api.dto.response

import shop.maeum.domain.member.entity.Member

data class FriendSimpleResDto(
    val memberId: String,
    val nickname: String,
    val email: String,
    val profileImageUrl: String
) {
    companion object {
        fun of(member: Member): FriendSimpleResDto {
            return FriendSimpleResDto(
                memberId = member.id!!,
                nickname = member.nickname,
                email = member.email,
                profileImageUrl = member.profilePath ?: ""
            )
        }
    }
}
