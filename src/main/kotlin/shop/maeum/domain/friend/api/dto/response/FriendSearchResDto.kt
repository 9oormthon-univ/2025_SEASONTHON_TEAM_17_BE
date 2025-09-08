package shop.maeum.domain.friend.api.dto.response

import shop.maeum.domain.friend.domain.FriendRelationStatus

data class FriendSearchResDto(
    val memberId: String,
    val email: String,
    val nickname: String,
    val profileImageUrl: String,
    val relationStatus: FriendRelationStatus
)