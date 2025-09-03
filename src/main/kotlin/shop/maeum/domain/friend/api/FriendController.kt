package shop.maeum.domain.friend.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import shop.maeum.domain.friend.api.dto.request.FriendEmailReqDto
import shop.maeum.domain.friend.api.dto.response.FriendSimpleResDto
import shop.maeum.domain.friend.application.FriendService
import shop.maeum.global.dto.CursorPageResDto
import shop.maeum.global.template.RspTemplate

@RestController
@RequestMapping("/api/v1/friends")
class FriendController(
    private val friendService: FriendService
) {

    @PostMapping("/request")
    fun requestFriend(@RequestBody request: FriendEmailReqDto): RspTemplate<Nothing?> {
        friendService.requestFriend(request.email)
        return RspTemplate(
            httpStatus = HttpStatus.CREATED,
            message = "친구 요청을 보냈습니다.",
            data = null
        )
    }

    @PostMapping("/accept")
    fun acceptFriend(@RequestBody request: FriendEmailReqDto): RspTemplate<Nothing?> {
        friendService.acceptFriend(request.email)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "친구 요청을 수락했습니다.",
            data = null
        )
    }

    @PostMapping("/reject")
    fun rejectFriend(@RequestBody request: FriendEmailReqDto): RspTemplate<Nothing?> {
        friendService.rejectFriend(request.email)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "친구 요청을 거절했습니다.",
            data = null
        )
    }

    @DeleteMapping("/cancel")
    fun cancelFriendRequest(@RequestParam toEmail: String): RspTemplate<Nothing?> {
        friendService.cancelFriendRequest(toEmail)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "친구 요청을 취소했습니다.",
            data = null
        )
    }

    @GetMapping("/received")
    fun getReceivedFriendRequests(): RspTemplate<List<FriendSimpleResDto>> {
        val result = friendService.getReceivedFriendRequests()
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "받은 친구 요청 목록 조회 성공",
            data = result
        )
    }

    @GetMapping("/sent")
    fun getSentFriendRequests(): RspTemplate<List<FriendSimpleResDto>> {
        val result = friendService.getSentFriendRequests()
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "보낸 친구 요청 목록 조회 성공",
            data = result
        )
    }

    @GetMapping
    fun getMyFriends(
        @RequestParam(required = false) cursor: String?,
        @RequestParam(defaultValue = "5") limit: Int
    ): RspTemplate<CursorPageResDto<FriendSimpleResDto>> {
        val result = friendService.getFriends(cursor, limit)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "친구 목록 조회 성공",
            data = result
        )
    }

//    @GetMapping("/search")
//    fun searchFriend(
//        @RequestParam(required = false) email: String?,
//        @RequestParam(required = false) nickname: String?
//    ): RspTemplate<List<FriendSearchResDto>> {
//        val result = friendService.searchFriend(email, nickname)
//        return RspTemplate(
//            httpStatus = HttpStatus.OK,
//            message = "친구 검색 성공",
//            data = result
//        )
//    }
}
