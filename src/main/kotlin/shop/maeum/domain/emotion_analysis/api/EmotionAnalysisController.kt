package shop.maeum.domain.emotion_analysis.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import shop.maeum.domain.diary.application.DiaryService
import shop.maeum.domain.emotion.application.EmotionLikeService
import shop.maeum.domain.emotion_analysis.application.EmotionAnalysisService
import shop.maeum.domain.emotion_analysis.application.dto.TwoWeeksEmotionAnalysisDataDto
import shop.maeum.domain.security.util.SecurityUtil
import shop.maeum.global.template.RspTemplate

@RestController
@RequestMapping("/api/v1/emotion-analysis")
class EmotionAnalysisController(
    val emotionAnalysisService: EmotionAnalysisService,
) : EmotionAnalysisDocs {

    @GetMapping("/emotions/report")
    fun getWeeklyDiaryTest(): RspTemplate<TwoWeeksEmotionAnalysisDataDto> {

        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "감정 분석 테스트 성공",
            emotionAnalysisService.analyzeWeeklyEmotions(SecurityUtil.getCurrentEmail())
        )
    }


}