package shop.maeum.domain.friend.exception

import shop.maeum.global.error.exception.InvalidGroupException

class FriendRequestInvalidException(
    message: String = "유효하지 않은 친구 요청입니다."
) : InvalidGroupException(message)
