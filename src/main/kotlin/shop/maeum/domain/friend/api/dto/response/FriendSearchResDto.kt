package shop.maeum.domain.friend.api.dto.response

data class FriendSearchResDto(
    val memberId: Long,
    val nickname: String,
    val email: String,
    val isFriend: Boolean,
    val isRequested: Boolean
)
