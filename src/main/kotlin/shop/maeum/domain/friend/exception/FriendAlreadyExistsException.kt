package shop.maeum.domain.friend.exception

import shop.maeum.global.error.exception.InvalidGroupException

class FriendAlreadyExistsException(
    message: String = "이미 존재하는 친구 요청입니다."
) : InvalidGroupException(message)
