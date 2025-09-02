package shop.maeum.domain.diary.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import shop.maeum.domain.diary.api.dto.request.WriteDiaryReqDto
import shop.maeum.domain.diary.api.dto.response.DiaryDetailResDto
import shop.maeum.domain.diary.api.dto.response.DiaryListWithPageResDto
import shop.maeum.domain.diary.api.dto.response.WriteDiaryResDto
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
        content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = RspTemplate::class),
                examples = [
                    ExampleObject(
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
                    )
                ]
            )
        ]
    )
    fun writeDiary(
        @RequestBody(
            required = true,
            description = "일기 작성 요청 DTO",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = WriteDiaryReqDto::class),
                    examples = [
                        ExampleObject(
                            name = "요청 예시",
                            value = """
                            {
                              "title": "오늘 하루",
                              "content": "정말 피곤한 하루였다.",
                              "privacySetting": "PRIVATE"
                            }
                            """
                        )
                    ]
                )
            ]
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
        content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = RspTemplate::class),
                examples = [
                    ExampleObject(
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
                    )
                ]
            )
        ]
    )
    fun getDiaryDetail(
        @Parameter(description = "조회할 일기의 ID")
        @PathVariable diaryId: Long
    ): RspTemplate<DiaryDetailResDto>


    @Operation(
        summary = "일기 목록 조회 (페이징)",
        description = "현재 로그인한 사용자의 일기 목록을 페이지 단위로 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "일기 목록 조회 성공",
        content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = RspTemplate::class),
                examples = [
                    ExampleObject(
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
                          "currentPage": 0,
                          "totalPages": 1,
                          "totalItems": 1
                        }
                      }
                    }
                    """
                    )
                ]
            )
        ]
    )
    fun getDiaries(
        @RequestParam(defaultValue = "0", name = "page") page: Int,
        @RequestParam(defaultValue = "10", name = "size") size: Int
    ): RspTemplate<DiaryListWithPageResDto>

    @Operation(
        summary = "일기 삭제",
        description = "선택한 일기를 삭제합니다. (Soft Delete)"
    )
    @ApiResponse(
        responseCode = "200",
        description = "일기 삭제 성공",
        content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = RspTemplate::class),
                examples = [
                    ExampleObject(
                        name = "일기 삭제 응답 예시",
                        value = """
                        {
                          "statusCode": 200,
                          "message": "일기가 성공적으로 삭제되었습니다.",
                          "data": null
                        }
                        """
                    )
                ]
            )
        ]
    )
    fun deleteDiary(
        diaryId: Long
    ): RspTemplate<Void>
}
