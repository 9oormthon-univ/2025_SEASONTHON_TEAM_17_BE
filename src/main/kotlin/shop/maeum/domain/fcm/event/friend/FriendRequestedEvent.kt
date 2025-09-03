package shop.maeum.domain.fcm.event.friend

data class FriendRequestedEvent(
    val fromMemberId: String,
    val fromMemberNickname: String,
    val toMemberId: String,
)
