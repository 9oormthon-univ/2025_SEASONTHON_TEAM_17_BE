package shop.maeum.domain.emotion.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shop.maeum.domain.emotion.application.EmotionLikeService
import shop.maeum.global.template.RspTemplate

@RestController
@RequestMapping("/api/v1/emotions")
class EmotionLikeController(
    private val emotionLikeService: EmotionLikeService
) : EmotionLikeDocs {
    @PostMapping("/{emotionId}/like")
    override fun likeEmotion(@PathVariable emotionId: Long): RspTemplate<String> {
        emotionLikeService.toggleEmotionLike(emotionId)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "감정 좋아요 성공",
            data = "liked"
        )
    }
}
