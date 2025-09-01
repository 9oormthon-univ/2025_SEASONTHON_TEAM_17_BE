package shop.maeum.domain.diary.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import shop.maeum.domain.diary.api.dto.request.WriteDiaryReqDto
import shop.maeum.domain.diary.api.dto.response.DiaryDetailResDto
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
        description = "특정 일기의 상세 정보를 조회합니다. (감정 리스트 포함)"
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
                            "feedback": "오늘 하루는 정말 피곤했겠네요. 내일은 더 나아질 거예요.",
                            "emotions": ["슬픔", "피곤함", "불안", "외로움", "무기력"]
                          }
                        }
                        """
                    )
                ]
            )
        ]
    )
    fun getDiaryDetail(
        diaryId: Long
    ): RspTemplate<DiaryDetailResDto>
}
