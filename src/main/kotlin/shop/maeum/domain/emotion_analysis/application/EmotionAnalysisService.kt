package shop.maeum.domain.emotion_analysis.application

import org.springframework.stereotype.Service
import shop.maeum.domain.diary.application.DiaryService
import shop.maeum.domain.emotion.application.EmotionLikeService
import shop.maeum.domain.emotion.domain.EmotionType
import shop.maeum.domain.emotion_analysis.application.dto.DiaryDataDto
import shop.maeum.domain.emotion_analysis.application.dto.EmotionAnalysisDataDto
import shop.maeum.domain.emotion_analysis.application.dto.EmotionDataDto
import shop.maeum.domain.emotion_analysis.application.dto.ExtractLikeRanksDto
import shop.maeum.domain.emotion_analysis.application.dto.TwoWeeksEmotionAnalysisDataDto
import shop.maeum.domain.member.application.MemberService
import java.time.LocalDateTime

@Service
class EmotionAnalysisService(
    private val emotionLikeService: EmotionLikeService,
    private val diaryService: DiaryService,
    private val memberService: MemberService
) {
    fun analyzeWeeklyEmotions(email: String) : TwoWeeksEmotionAnalysisDataDto {
        val thisWeek = LocalDateTime.now().let { end ->
            val start = end.minusDays(6)
            Pair(start, end)
        }
        val lastWeek = thisWeek.first.minusDays(1).let { end ->
            val start = end.minusDays(6)
            Pair(start, end)
        }
        val member = memberService.getByEmail(email)
        val thisWeekDiaries = diaryService.getWeeklyDiaries(member, thisWeek.first, thisWeek.second)
        val lastWeekDiaries = diaryService.getWeeklyDiaries(member, lastWeek.first, lastWeek.second)

        val (diaryDataList, thisEmotionDataList) = separateEmotionsAndDiaries(thisWeekDiaries)
        val (lastDiaryDataList, lastEmotionDataList) = separateEmotionsAndDiaries(lastWeekDiaries)

        return TwoWeeksEmotionAnalysisDataDto(
            analyzeEmotionsByFriendsLikes(thisEmotionDataList),
            analyzeEmotionsByFriendsLikes(lastEmotionDataList)
        )
    }

    fun separateEmotionsAndDiaries(emotionAnalysisDataDtoList: List<EmotionAnalysisDataDto>)
    : Pair<List<DiaryDataDto>, List<EmotionDataDto>> {

        val diaryDataList = emotionAnalysisDataDtoList.map { diary ->
            DiaryDataDto(
                diaryId = diary.diaryId,
                content = diary.content
            )
        }

        val emotionDataList = emotionAnalysisDataDtoList.flatMap { diary ->
            diary.emotionList.map { emotion ->
                EmotionDataDto(
                    emotionId = emotion.emotionId,
                    emotionType = emotion.emotionType
                )
            }
        }
        return Pair(diaryDataList, emotionDataList)
    }

    fun analyzeEmotionsByFriendsLikes(emotionList: List<EmotionDataDto>): List<ExtractLikeRanksDto> {
        val countedEmotionList = emotionLikeService.getEmotionLikesByFriends(emotionList)
            .groupBy { it.second }
            .mapValues { entry ->
                entry.value.sumOf { it.third }
            }
            .toMap()

        return EmotionType.entries.map { emotion ->
            val count = countedEmotionList[emotion] ?: 0
            ExtractLikeRanksDto(emotion, count)
        }
    }

    fun analyzeKeywordByDiary( diaryList: List<DiaryDataDto>) {
        //TODO 추후 구현 예정        
    }
}