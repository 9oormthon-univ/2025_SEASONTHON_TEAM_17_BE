package shop.maeum.domain.friend.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import shop.maeum.domain.friend.api.dto.request.FriendEmailReqDto
import shop.maeum.domain.friend.api.dto.response.FriendSearchResDto
import shop.maeum.domain.friend.api.dto.response.FriendSimpleResDto
import shop.maeum.domain.friend.application.FriendService
import shop.maeum.global.dto.CursorPageResDto
import shop.maeum.global.template.RspTemplate

@RestController
@RequestMapping("/api/v1/friends")
class FriendController(
    private val friendService: FriendService
) : FriendDocs {

    @PostMapping("/request")
    override fun requestFriend(@RequestBody request: FriendEmailReqDto): RspTemplate<Nothing?> {
        friendService.requestFriend(request.email)
        return RspTemplate(
            httpStatus = HttpStatus.CREATED,
            message = "친구 요청을 보냈습니다.",
            data = null
        )
    }

    @PostMapping("/accept")
    override fun acceptFriend(@RequestBody request: FriendEmailReqDto): RspTemplate<Nothing?> {
        friendService.acceptFriend(request.email)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "친구 요청을 수락했습니다.",
            data = null
        )
    }

    @PostMapping("/reject")
    override fun rejectFriend(@RequestBody request: FriendEmailReqDto): RspTemplate<Nothing?> {
        friendService.rejectFriend(request.email)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "친구 요청을 거절했습니다.",
            data = null
        )
    }

    @DeleteMapping("/cancel")
    override fun cancelFriendRequest(@RequestParam toEmail: String): RspTemplate<Nothing?> {
        friendService.cancelFriendRequest(toEmail)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "친구 요청을 취소했습니다.",
            data = null
        )
    }

    @DeleteMapping
    override fun removeFriend(@RequestParam email: String): RspTemplate<Nothing?> {
        friendService.removeFriend(email)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "친구 관계를 삭제했습니다.",
            data = null
        )
    }

    @GetMapping("/received")
    override fun getReceivedFriendRequests(
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "5") limit: Int
    ): RspTemplate<CursorPageResDto<FriendSimpleResDto, Long>> {
        val result = friendService.getReceivedFriendRequests(cursor, limit)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "받은 친구 요청 목록 조회 성공",
            data = result
        )
    }

    @GetMapping("/sent")
    override fun getSentFriendRequests(
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "5") limit: Int
    ): RspTemplate<CursorPageResDto<FriendSimpleResDto, Long>> {
        val result = friendService.getSentFriendRequests(cursor, limit)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "보낸 친구 요청 목록 조회 성공",
            data = result
        )
    }

    @GetMapping
    override fun getMyFriends(
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "5") limit: Int
    ): RspTemplate<CursorPageResDto<FriendSimpleResDto, Long>> {
        val result = friendService.getFriends(cursor, limit)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "친구 목록 조회 성공",
            data = result
        )
    }

    @GetMapping("/search")
    override fun searchFriends(
        @RequestParam keyword: String,
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "5") limit: Int
    ): CursorPageResDto<FriendSearchResDto, String> {
        return friendService.searchFriendWithCursor(keyword, cursor, limit)
    }
}
