package shop.maeum.domain.friend.domain

enum class FriendRelationStatus {
    FRIEND,             // 서로 친구
    REQUESTED_BY_ME,    // 내가 요청 보냄
    REQUESTED_BY_OTHER, // 상대가 나에게 요청함
    NONE                // 아무런 관계 없음
}