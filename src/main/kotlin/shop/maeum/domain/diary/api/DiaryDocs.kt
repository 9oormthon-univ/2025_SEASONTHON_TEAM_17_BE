package shop.maeum.domain.diary.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import shop.maeum.domain.diary.api.dto.request.WriteDiaryReqDto
import shop.maeum.domain.diary.api.dto.response.*
import shop.maeum.global.dto.CursorPageResDto
import shop.maeum.global.template.RspTemplate

@Tag(name = "Diary", description = "일기 관련 API")
interface DiaryDocs {

    @Operation(
        summary = "일기 작성",
        description = "일기를 작성하고 AI 분석 결과(피드백, 감정 5개)를 함께 반환합니다."
    )
    @ApiResponse(
        responseCode = "201",
        description = "일기 작성 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                name = "일기 작성 성공 응답 예시",
                value = """
                {
                  "statusCode": 201,
                  "message": "일기가 성공적으로 작성되었습니다.",
                  "data": {
                    "diaryId": 1,
                    "title": "오늘 하루",
                    "content": "정말 피곤한 하루였다.",
                    "privacySetting": "PRIVATE",
                    "feedback": "오늘 하루는 정말 피곤했겠네요. 충분한 휴식을 취하시길 바랍니다."
                  }
                }
                """
            )]
        )]
    )
    fun writeDiary(
        @RequestBody(
            required = true,
            description = "일기 작성 요청 DTO",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = WriteDiaryReqDto::class),
                examples = [ExampleObject(
                    name = "요청 예시",
                    value = """
                    {
                      "title": "오늘 하루",
                      "content": "정말 피곤한 하루였다.",
                      "privacySetting": "PRIVATE"
                    }
                    """
                )]
            )]
        )
        writeDiaryReqDto: WriteDiaryReqDto
    ): RspTemplate<WriteDiaryResDto>

    @Operation(
        summary = "일기 상세 조회",
        description = "특정 일기의 상세 정보를 조회합니다. (감정 리스트 및 각 감정의 좋아요 수 포함)"
    )
    @ApiResponse(
        responseCode = "200",
        description = "일기 상세 조회 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                name = "일기 상세 조회 성공 응답 예시",
                value = """
                {
                  "statusCode": 200,
                  "message": "일기 상세 조회 성공",
                  "data": {
                    "diaryId": 1,
                    "title": "오늘 하루",
                    "content": "정말 피곤한 하루였다.",
                    "privacySetting": "PRIVATE",
                    "feedback": "오늘 하루는 정말 피곤했겠네요. 내일은 더 나아질 거예요.",
                    "emotions": [
                      {
                        "emotionId": 10,
                        "type": "슬픔",
                        "likeCount": 3
                      },
                      {
                        "emotionId": 11,
                        "type": "피곤함",
                        "likeCount": 5
                      },
                      {
                        "emotionId": 12,
                        "type": "불안",
                        "likeCount": 1
                      },
                      {
                        "emotionId": 13,
                        "type": "외로움",
                        "likeCount": 0
                      },
                      {
                        "emotionId": 14,
                        "type": "무기력",
                        "likeCount": 2
                      }
                    ]
                  }
                }
                """
            )]
        )]
    )
    fun getDiaryDetail(
        @Parameter(description = "조회할 일기의 ID")
        @PathVariable diaryId: Long
    ): RspTemplate<DiaryDetailResDto>

    @Operation(
        summary = "일기 목록 조회 (커서 기반)",
        description = "현재 로그인한 사용자의 일기를 최신순으로 커서 기반 페이징하여 조회합니다. 기본 limit=3"
    )
    @ApiResponse(
        responseCode = "200",
        description = "일기 목록 조회 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                name = "일기 목록 조회 예시",
                value = """
                {
                  "statusCode": 200,
                  "message": "일기 목록 조회 성공",
                  "data": {
                    "diaries": [
                      {
                        "diaryId": 1,
                        "title": "오늘 하루",
                        "content": "정말 피곤한 하루였다.",
                        "feedback": "오늘 하루는 정말 피곤했겠네요.",
                        "createdAt": "2025-09-02T14:30:00"
                      }
                    ],
                    "pageInfo": {
                      "nextCursor": 12,
                      "hasNext": true
                    }
                  }
                }
                """
            )]
        )]
    )
    fun getDiaries(
        @Parameter(description = "마지막으로 조회한 일기의 ID (없으면 첫 페이지)")
        @RequestParam(required = false) cursor: Long?,
        @Parameter(description = "가져올 일기의 개수 (기본값 3)")
        @RequestParam(defaultValue = "3") limit: Int
    ): RspTemplate<CursorPageResDto<DiarySummaryResDto, Long>>

    @Operation(
        summary = "일기 삭제",
        description = "선택한 일기를 삭제합니다. (Soft Delete)"
    )
    @ApiResponse(
        responseCode = "200",
        description = "일기 삭제 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                name = "일기 삭제 응답 예시",
                value = """
                {
                  "statusCode": 200,
                  "message": "일기가 성공적으로 삭제되었습니다.",
                  "data": null
                }
                """
            )]
        )]
    )
    fun deleteDiary(
        @Parameter(description = "삭제할 일기의 ID")
        @PathVariable diaryId: Long
    ): RspTemplate<Void>

    @Operation(
        summary = "월별 일기 작성 날짜 조회",
        description = "특정 연도와 월에 사용자가 작성한 일기의 날짜들을 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "월별 일기 날짜 목록 조회 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                name = "월별 작성 일기 날짜 응답 예시",
                value = """
                {
                  "statusCode": 200,
                  "message": "해당 월의 작성된 일기 날짜 목록 조회 성공",
                  "data": [1, 3, 5, 10, 18, 25]
                }
                """
            )]
        )]
    )
    @GetMapping("/month")
    fun getWrittenDaysInMonth(
        @Parameter(description = "연도 (예: 2025)") @RequestParam year: Int,
        @Parameter(description = "월 (1~12)") @RequestParam month: Int
    ): RspTemplate<List<Int>>

    @Operation(
        summary = "특정 날짜의 일기 조회",
        description = "특정 연, 월, 일에 작성된 일기를 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "특정 날짜의 일기 조회 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                name = "특정 날짜 일기 조회 응답 예시",
                value = """
            {
              "statusCode": 200,
              "message": "특정 날짜의 일기 조회 성공",
              "data": {
                "diaryId": 5,
                "title": "가을의 시작",
                "content": "선선한 바람이 기분 좋은 하루였다.",
                "privacySetting": "PUBLIC",
                "feedback": "선선한 날씨가 기분을 좋게 했나봐요.",
                "emotions": [
                  {
                    "emotionId": 21,
                    "type": "기쁨",
                    "likeCount": 4
                  },
                  {
                    "emotionId": 22,
                    "type": "여유",
                    "likeCount": 2
                  },
                  {
                    "emotionId": 23,
                    "type": "평온",
                    "likeCount": 1
                  },
                  {
                    "emotionId": 24,
                    "type": "행복",
                    "likeCount": 3
                  },
                  {
                    "emotionId": 25,
                    "type": "감사",
                    "likeCount": 5
                  }
                ]
              }
            }
            """
            )]
        )]
    )
    @GetMapping("/date")
    fun getDiaryByDate(
        @Parameter(description = "연도 (예: 2025)") @RequestParam year: Int,
        @Parameter(description = "월 (1~12)") @RequestParam month: Int,
        @Parameter(description = "일 (1~31)") @RequestParam day: Int
    ): RspTemplate<DiaryDetailResDto>

    @Operation(
        summary = "오늘 일기 작성 여부 확인",
        description = "오늘 날짜에 사용자가 일기를 작성했는지 여부 및 작성된 경우 일기 요약 정보를 반환합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "오늘 일기 작성 여부 확인 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                name = "오늘 일기 작성 응답 예시 (작성한 경우)",
                value = """
            {
              "statusCode": 200,
              "message": "오늘 작성한 일기가 있습니다.",
              "data": {
                "written": true,
                "diaryId": 42,
                "title": "바쁜 하루",
                "preview": "오늘은 회의가 많아서 정신없었다..."
              }
            }
            """
            ),
                ExampleObject(
                    name = "오늘 일기 작성 응답 예시 (작성하지 않은 경우)",
                    value = """
            {
              "statusCode": 200,
              "message": "오늘 작성한 일기가 없습니다.",
              "data": {
                "written": false,
                "diaryId": null,
                "title": null,
                "preview": null
              }
            }
            """
                )]
        )]
    )
    @GetMapping("/today")
    fun hasDiaryToday(): RspTemplate<DiaryTodayResDto>

}
