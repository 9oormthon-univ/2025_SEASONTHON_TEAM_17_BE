package shop.maeum.domain.diary.application

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shop.maeum.domain.ai.application.AiService
import shop.maeum.domain.diary.api.dto.request.WriteDiaryReqDto
import shop.maeum.domain.diary.domain.Diary
import shop.maeum.domain.diary.domain.repository.DiaryRepository
import shop.maeum.global.entity.Status
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import shop.maeum.domain.diary.api.dto.response.*
import shop.maeum.domain.diary.exception.DiaryNotFoundException
import shop.maeum.domain.emotion.domain.Emotion
import shop.maeum.domain.emotion.domain.EmotionType
import shop.maeum.domain.member.repository.MemberRepository
import shop.maeum.domain.security.util.SecurityUtil
import shop.maeum.global.dto.CursorPageResDto
import shop.maeum.global.dto.PageInfoResDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
@Transactional(readOnly = true)
class DiaryService(
    private val diaryRepository: DiaryRepository,
    private val aiService: AiService,
    private val memberRepository: MemberRepository,
    private val securityUtil: SecurityUtil,

    @Value("\${ai.prompt.diary-analysis}")
    private val diaryAnalysisPrompt: String
) {
    private val log = LoggerFactory.getLogger(DiaryService::class.java)

    @Transactional
    fun writeDiary(writeDiaryReqDto: WriteDiaryReqDto): WriteDiaryResDto {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with id ${securityUtil.getCurrentEmail()} not found")

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
            status = Status.ACTIVE,
            member = member
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

    fun getDiaries(cursor: Long?, limit: Int = 3): CursorPageResDto<DiarySummaryResDto> {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with email ${securityUtil.getCurrentEmail()} not found")

        val diaries = diaryRepository.findAllByMemberWithCursor(
            memberId = member.id!!,
            cursor = cursor,
            pageable = PageRequest.of(0, limit + 1)
        )

        val hasNext = diaries.size > limit
        val sliced = diaries.take(limit)

        return CursorPageResDto(
            data = sliced.map { DiarySummaryResDto.fromEntity(it) },
            nextCursor = if (hasNext) sliced.last().id else null,
            hasNext = hasNext
        )
    }

    fun getDiaryDetail(diaryId: Long): DiaryDetailResDto {
        val diary = diaryRepository.findByIdOrNull(diaryId)
            ?: throw DiaryNotFoundException()

        return DiaryDetailResDto.fromEntity(diary)
    }

    @Transactional
    fun deleteDiary(diaryId: Long) {
        val diary = diaryRepository.findByIdOrNull(diaryId)
            ?: throw DiaryNotFoundException()

        diaryRepository.delete(diary)
    }

    fun getWrittenDaysInMonth(year: Int, month: Int): List<Int> {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with email ${securityUtil.getCurrentEmail()} not found")

        val start = LocalDate.of(year, month, 1).atStartOfDay()
        val end = start.withDayOfMonth(start.toLocalDate().lengthOfMonth()).with(LocalTime.MAX)

        val diaries = diaryRepository.findAllByMemberIdAndCreatedAtBetween(member.id!!, start, end)

        return diaries.map { it.createdAt!!.dayOfMonth }.distinct()
    }

    fun getDiaryByDate(year: Int, month: Int, day: Int): DiaryDetailResDto {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with email ${securityUtil.getCurrentEmail()} not found")

        val start = LocalDateTime.of(year, month, day, 0, 0)
        val end = LocalDateTime.of(year, month, day, 23, 59, 59)

        val diary = diaryRepository.findFirstByMemberIdAndCreatedAtBetween(member.id!!, start, end)
            ?: throw DiaryNotFoundException()

        return DiaryDetailResDto.fromEntity(diary)
    }

    fun hasDiaryToday(): DiaryTodayResDto {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member not found")

        val today = LocalDate.now()
        val start = today.atStartOfDay()
        val end = today.atTime(LocalTime.MAX)

        val diary = diaryRepository.findFirstByMemberIdAndCreatedAtBetween(member.id!!, start, end)

        return if (diary != null) {
            DiaryTodayResDto(
                written = true,
                diaryId = diary.id,
                title = diary.title,
                preview = diary.content.take(10)
            )
        } else {
            DiaryTodayResDto(written = false)
        }
    }
}