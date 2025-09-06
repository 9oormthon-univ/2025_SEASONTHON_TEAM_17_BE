package shop.maeum.domain.emotion_analysis.application.dto

data class TwoWeeksEmotionAnalysisDataDto(
    val thisWeekEmotionData: List<ExtractLikeRanksDto>?,
    val lastWeekEmotionData: List<ExtractLikeRanksDto>?
)