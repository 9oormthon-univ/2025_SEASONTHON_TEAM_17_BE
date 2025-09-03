package shop.maeum.domain.friend.api.dto.response

data class FriendSearchResDto(
    val memberId: String,
    val nickname: String,
    val email: String,
    val profileImageUrl: String,
    val isFriend: Boolean,
    val isRequested: Boolean
)
