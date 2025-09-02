package shop.maeum.domain.emotion.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.PathVariable
import shop.maeum.global.template.RspTemplate

@Tag(name = "Emotion", description = "감정 관련 API (좋아요 등)")
interface EmotionLikeDocs {

    @Operation(
        summary = "감정 좋아요 토글",
        description = "해당 감정에 대해 좋아요를 누르거나 취소합니다. 한 번 누르면 좋아요, 다시 누르면 취소됩니다." +
                "또, 같은 일기 안에서 다른 감정을 누르면 자동으로 이전 좋아요가 취소되고 새로 좋아요가 등록됩니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "감정 좋아요 토글 성공",
        content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = RspTemplate::class),
                examples = [
                    ExampleObject(
                        name = "감정 좋아요 응답 예시",
                        value = """
                        {
                          "statusCode": 200,
                          "message": "감정 좋아요 성공",
                          "data": "liked"
                        }
                        """
                    )
                ]
            )
        ]
    )
    fun likeEmotion(
        @Parameter(description = "좋아요를 누를 감정의 ID")
        @PathVariable emotionId: Long
    ): RspTemplate<String>
}
