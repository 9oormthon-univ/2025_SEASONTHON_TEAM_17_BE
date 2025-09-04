package shop.maeum.domain.friend.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import shop.maeum.domain.friend.api.dto.request.FriendEmailReqDto
import shop.maeum.domain.friend.api.dto.response.FriendSearchResDto
import shop.maeum.domain.friend.api.dto.response.FriendSimpleResDto
import shop.maeum.global.dto.CursorPageResDto
import shop.maeum.global.template.RspTemplate

@Tag(name = "Friend", description = "친구 관련 API")
interface FriendDocs {

    @Operation(
        summary = "친구 요청",
        description = "이메일을 통해 친구 요청을 보냅니다."
    )
    @ApiResponse(
        responseCode = "201",
        description = "친구 요청 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                value = """
                {
                  "statusCode": 201,
                  "message": "친구 요청을 보냈습니다.",
                  "data": null
                }
                """
            )]
        )]
    )
    @PostMapping("/request")
    fun requestFriend(
        @RequestBody(
            required = true,
            description = "친구 요청 대상 회원 이메일",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = FriendEmailReqDto::class),
                examples = [ExampleObject(
                    value = """
                    {
                      "email": "friend@example.com"
                    }
                    """
                )]
            )]
        )
        request: FriendEmailReqDto
    ): RspTemplate<Nothing?>

    @Operation(
        summary = "친구 요청 수락",
        description = "받은 친구 요청을 수락합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "친구 요청 수락 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                value = """
                {
                  "statusCode": 200,
                  "message": "친구 요청을 수락했습니다.",
                  "data": null
                }
                """
            )]
        )]
    )
    @PostMapping("/accept")
    fun acceptFriend(
        @RequestBody(
            required = true,
            description = "친구 요청을 보낸 회원 이메일",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = FriendEmailReqDto::class),
                examples = [ExampleObject(
                    value = """
                    {
                      "email": "friend@example.com"
                    }
                    """
                )]
            )]
        )
        request: FriendEmailReqDto
    ): RspTemplate<Nothing?>

    @Operation(
        summary = "친구 요청 거절",
        description = "받은 친구 요청을 거절합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "친구 요청 거절 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                value = """
                {
                  "statusCode": 200,
                  "message": "친구 요청을 거절했습니다.",
                  "data": null
                }
                """
            )]
        )]
    )
    @PostMapping("/reject")
    fun rejectFriend(
        @RequestBody(
            required = true,
            description = "친구 요청을 보낸 회원 이메일",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = FriendEmailReqDto::class),
                examples = [ExampleObject(
                    value = """
                    {
                      "email": "friend@example.com"
                    }
                    """
                )]
            )]
        )
        request: FriendEmailReqDto
    ): RspTemplate<Nothing?>

    @Operation(
        summary = "친구 요청 취소",
        description = "내가 보낸 친구 요청을 취소합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "친구 요청 취소 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                value = """
                {
                  "statusCode": 200,
                  "message": "친구 요청을 취소했습니다.",
                  "data": null
                }
                """
            )]
        )]
    )
    @DeleteMapping("/cancel")
    fun cancelFriendRequest(
        @Parameter(description = "취소할 친구 요청 대상 이메일") @RequestParam toEmail: String
    ): RspTemplate<Nothing?>

    @Operation(
        summary = "친구 삭제",
        description = "현재 로그인한 사용자가 친구 관계를 해제합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "친구 삭제 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                value = """
                {
                  "statusCode": 200,
                  "message": "친구 관계를 삭제했습니다.",
                  "data": null
                }
                """
            )]
        )]
    )
    @DeleteMapping
    fun removeFriend(
        @Parameter(description = "삭제할 친구 이메일") @RequestParam email: String
    ): RspTemplate<Nothing?>

    @Operation(
        summary = "받은 친구 요청 목록 조회",
        description = "현재 로그인한 사용자가 받은 친구 요청 목록을 커서 기반으로 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "받은 친구 요청 목록 조회 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                value = """
                {
                  "statusCode": 200,
                  "message": "받은 친구 요청 목록 조회 성공",
                  "data": {
                    "data": [
                      {
                        "memberId": 1,
                        "nickname": "friend1",
                        "email": "friend1@example.com",
                        "profileImageUrl": "https://cdn.maeum.shop/f1.png"
                      }
                    ],
                    "nextCursor": 100,
                    "hasNext": true
                  }
                }
                """
            )]
        )]
    )
    @GetMapping("/received")
    fun getReceivedFriendRequests(
        @Parameter(description = "커서 ID") @RequestParam(required = false) cursor: Long?,
        @Parameter(description = "가져올 개수 (기본 5)") @RequestParam(defaultValue = "5") limit: Int
    ): RspTemplate<CursorPageResDto<FriendSimpleResDto, Long>>

    @Operation(
        summary = "보낸 친구 요청 목록 조회",
        description = "현재 로그인한 사용자가 보낸 친구 요청 목록을 커서 기반으로 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "보낸 친구 요청 목록 조회 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                value = """
                {
                  "statusCode": 200,
                  "message": "보낸 친구 요청 목록 조회 성공",
                  "data": {
                    "data": [
                      {
                        "memberId": 2,
                        "nickname": "friend2",
                        "email": "friend2@example.com",
                        "profileImageUrl": "https://cdn.maeum.shop/f2.png"
                      }
                    ],
                    "nextCursor": 200,
                    "hasNext": false
                  }
                }
                """
            )]
        )]
    )
    @GetMapping("/sent")
    fun getSentFriendRequests(
        @Parameter(description = "커서 ID") @RequestParam(required = false) cursor: Long?,
        @Parameter(description = "가져올 개수 (기본 5)") @RequestParam(defaultValue = "5") limit: Int
    ): RspTemplate<CursorPageResDto<FriendSimpleResDto, Long>>

    @Operation(
        summary = "친구 목록 조회",
        description = "현재 로그인한 사용자의 친구 목록을 커서 기반으로 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "친구 목록 조회 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                value = """
                {
                  "statusCode": 200,
                  "message": "친구 목록 조회 성공",
                  "data": {
                    "data": [
                      {
                        "memberId": 3,
                        "nickname": "bestie",
                        "email": "bestie@example.com",
                        "profileImageUrl": "https://cdn.maeum.shop/f3.png"
                      }
                    ],
                    "nextCursor": null,
                    "hasNext": false
                  }
                }
                """
            )]
        )]
    )
    @GetMapping
    fun getMyFriends(
        @Parameter(description = "커서 ID") @RequestParam(required = false) cursor: Long?,
        @Parameter(description = "가져올 개수 (기본 5)") @RequestParam(defaultValue = "5") limit: Int
    ): RspTemplate<CursorPageResDto<FriendSimpleResDto, Long>>

    @Operation(
        summary = "친구 검색",
        description = "이메일 또는 닉네임에 키워드가 포함된 회원을 검색합니다. 클라이언트에서 입력이 끝난 후 2초 정도 기다리고 호출해주세요. 본인은 제외 조회됩니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "전체 회원 검색 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                value = """
                {
                  "statusCode": 200,
                  "message": "전체 회원 검색 성공",
                  "data": {
                    "data": [
                      {
                        "memberId": 123,
                        "nickname": "newbie",
                        "email": "newbie@example.com",
                        "profileImageUrl": "https://cdn.maeum.shop/u123.png",
                        "isFriend": false,
                        "isRequested": true
                      }
                    ],
                    "nextCursor": 456,
                    "hasNext": true
                  }
                }
                """
            )]
        )]
    )
    @GetMapping("/search")
    fun searchFriends(
        @Parameter(description = "검색 키워드 (2자 이상)") @RequestParam keyword: String,
        @Parameter(description = "커서 ID") @RequestParam(required = false) cursor: Long?,
        @Parameter(description = "가져올 개수 (기본 5)") @RequestParam(defaultValue = "5") limit: Int
    ): RspTemplate<CursorPageResDto<FriendSearchResDto, Long>>

    @Operation(
        summary = "내 친구 목록 검색",
        description = "내 친구 목록에서 이메일 또는 닉네임에 키워드가 포함된 친구를 검색합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "내 친구 목록 검색 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                value = """
                {
                  "statusCode": 200,
                  "message": "내 친구 목록 검색 성공",
                  "data": {
                    "data": [
                      {
                        "memberId": 3,
                        "nickname": "bestie",
                        "email": "bestie@example.com",
                        "profileImageUrl": "https://cdn.maeum.shop/f3.png"
                      }
                    ],
                    "nextCursor": null,
                    "hasNext": false
                  }
                }
                """
            )]
        )]
    )
    @GetMapping("/search/my-friends")
    fun searchMyFriends(
        @Parameter(description = "검색 키워드") @RequestParam keyword: String,
        @Parameter(description = "커서 ID") @RequestParam(required = false) cursor: Long?,
        @Parameter(description = "가져올 개수 (기본 5)") @RequestParam(defaultValue = "5") limit: Int
    ): RspTemplate<CursorPageResDto<FriendSimpleResDto, Long>>

    @Operation(
        summary = "보낸 친구 요청 목록 검색",
        description = "보낸 친구 요청 목록에서 이메일 또는 닉네임에 키워드가 포함된 요청을 검색합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "보낸 친구 요청 목록 검색 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                value = """
                {
                  "statusCode": 200,
                  "message": "보낸 친구 요청 목록 검색 성공",
                  "data": {
                    "data": [
                      {
                        "memberId": 2,
                        "nickname": "friend2",
                        "email": "friend2@example.com",
                        "profileImageUrl": "https://cdn.maeum.shop/f2.png"
                      }
                    ],
                    "nextCursor": 200,
                    "hasNext": false
                  }
                }
                """
            )]
        )]
    )
    @GetMapping("/search/sent")
    fun searchSentRequests(
        @Parameter(description = "검색 키워드") @RequestParam keyword: String,
        @Parameter(description = "커서 ID") @RequestParam(required = false) cursor: Long?,
        @Parameter(description = "가져올 개수 (기본 5)") @RequestParam(defaultValue = "5") limit: Int
    ): RspTemplate<CursorPageResDto<FriendSimpleResDto, Long>>

    @Operation(
        summary = "받은 친구 요청 목록 검색",
        description = "받은 친구 요청 목록에서 이메일 또는 닉네임에 키워드가 포함된 요청을 검색합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "받은 친구 요청 목록 검색 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                value = """
                {
                  "statusCode": 200,
                  "message": "받은 친구 요청 목록 검색 성공",
                  "data": {
                    "data": [
                      {
                        "memberId": 1,
                        "nickname": "friend1",
                        "email": "friend1@example.com",
                        "profileImageUrl": "https://cdn.maeum.shop/f1.png"
                      }
                    ],
                    "nextCursor": 100,
                    "hasNext": true
                  }
                }
                """
            )]
        )]
    )
    @GetMapping("/search/received")
    fun searchReceivedRequests(
        @Parameter(description = "검색 키워드") @RequestParam keyword: String,
        @Parameter(description = "커서 ID") @RequestParam(required = false) cursor: Long?,
        @Parameter(description = "가져올 개수 (기본 5)") @RequestParam(defaultValue = "5") limit: Int
    ): RspTemplate<CursorPageResDto<FriendSimpleResDto, Long>>
}