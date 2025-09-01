package shop.maeum.domain.diary.application

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shop.maeum.domain.ai.application.AiService
import shop.maeum.domain.diary.api.dto.request.WriteDiaryReqDto
import shop.maeum.domain.diary.api.dto.response.WriteDiaryResDto
import shop.maeum.domain.diary.domain.Diary
import shop.maeum.domain.diary.domain.repository.DiaryRepository
import shop.maeum.global.entity.Status
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import shop.maeum.domain.diary.api.dto.response.DiaryDetailResDto
import shop.maeum.domain.diary.api.dto.response.DiarySummaryResDto
import shop.maeum.domain.diary.exception.DiaryNotFoundException
import shop.maeum.domain.emotion.domain.Emotion
import shop.maeum.domain.emotion.domain.EmotionType

@Service
@Transactional(readOnly = true)
class DiaryService(
    private val diaryRepository: DiaryRepository,
    private val aiService: AiService,

    @Value("\${ai.prompt.diary-analysis}")
    private val diaryAnalysisPrompt: String
) {
    private val log = LoggerFactory.getLogger(DiaryService::class.java)

    @Transactional
    fun writeDiary(writeDiaryReqDto: WriteDiaryReqDto): WriteDiaryResDto {
        val fullPrompt = """
        $diaryAnalysisPrompt

        제목: ${writeDiaryReqDto.title}
        내용: ${writeDiaryReqDto.content}
    """.trimIndent()

        log.info("📜 최종 AI 프롬프트:\n$fullPrompt")

        val aiRawResponse = aiService.generate(fullPrompt)

        log.info("🧠 AI 응답 원문:\n$aiRawResponse")

        val (emotionNames, feedback) = parseAiResponse(aiRawResponse)

        val diary = Diary(
            title = writeDiaryReqDto.title,
            content = writeDiaryReqDto.content,
            privacySetting = writeDiaryReqDto.privacySetting,
            feedback = feedback,
            status = Status.ACTIVE
        )

        val emotions = emotionNames.map { name ->
            Emotion(
                emotionType = EmotionType.valueOf(name),
                diary = diary
            )
        }
        diary.emotions.addAll(emotions)

        diaryRepository.save(diary)

        return WriteDiaryResDto.fromEntity(diary)
    }

    private fun parseAiResponse(raw: String): Pair<List<String>, String> {
        val emotionRegex = Regex("""감정:\s*\[(.*?)\]""")
        val feedbackRegex = Regex("""피드백:\s*(.*)""")

        val emotions = emotionRegex.find(raw)
            ?.groups?.get(1)
            ?.value
            ?.split(",")
            ?.map { it.trim() }
            ?: emptyList()

        val feedback = feedbackRegex.find(raw)
            ?.groups?.get(1)
            ?.value
            ?.trim()
            ?: ""

        return emotions to feedback
    }

//    fun getDiaries(memberId: Long): List<DiarySummaryResDto> {
//        return diaryRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId)
//            .map { DiarySummaryResDto.fromEntity(it) }
//    }

    fun getDiaryDetail(diaryId: Long): DiaryDetailResDto {
        val diary = diaryRepository.findByIdOrNull(diaryId)
            ?: throw DiaryNotFoundException()

        return DiaryDetailResDto.fromEntity(diary)
    }
}