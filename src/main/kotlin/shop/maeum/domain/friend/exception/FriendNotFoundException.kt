package shop.maeum.domain.friend.exception

import shop.maeum.global.error.exception.NotFoundGroupException

class FriendNotFoundException(
    message: String = "존재하지 않는 친구 관계입니다."
) : NotFoundGroupException(message)
