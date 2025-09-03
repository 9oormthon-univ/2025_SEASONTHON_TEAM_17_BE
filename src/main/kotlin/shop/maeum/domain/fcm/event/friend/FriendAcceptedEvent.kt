package shop.maeum.domain.fcm.event.friend

data class FriendAcceptedEvent(
    val fromMemberId: String,
    val toMemberId: String,
    val toMemberNickname: String,
)