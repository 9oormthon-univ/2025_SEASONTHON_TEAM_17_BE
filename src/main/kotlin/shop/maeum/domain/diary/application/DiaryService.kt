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

        val (emotions, feedback) = parseAiResponse(aiRawResponse)

        val diary = Diary(
            title = writeDiaryReqDto.title,
            content = writeDiaryReqDto.content,
            privacySetting = writeDiaryReqDto.privacySetting,
            feedback = feedback,
            status = Status.ACTIVE
        )
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

}
