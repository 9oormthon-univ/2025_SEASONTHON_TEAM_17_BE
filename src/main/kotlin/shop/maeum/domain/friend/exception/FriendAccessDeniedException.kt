package shop.maeum.domain.friend.exception

import shop.maeum.global.error.exception.AccessDeniedGroupException

class FriendAccessDeniedException(
    message: String = "해당 친구 요청에 접근할 수 없습니다."
) : AccessDeniedGroupException(message)
