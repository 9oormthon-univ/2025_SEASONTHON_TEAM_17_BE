package shop.maeum.domain.diary.application

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shop.maeum.domain.ai.application.AiService
import shop.maeum.domain.diary.api.dto.request.WriteDiaryReqDto
import shop.maeum.domain.diary.api.dto.request.WriteDiaryWithDateReqDto
import shop.maeum.domain.diary.domain.Diary
import shop.maeum.domain.diary.domain.repository.DiaryRepository
import shop.maeum.global.entity.Status
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import shop.maeum.domain.diary.api.dto.response.*
import shop.maeum.domain.diary.exception.DiaryNotFoundException
import shop.maeum.domain.emotion.domain.Emotion
import shop.maeum.domain.emotion.domain.EmotionType
import shop.maeum.domain.emotion_analysis.application.dto.EmotionAnalysisDataDto
import shop.maeum.domain.member.application.MemberService
import shop.maeum.domain.member.entity.Member
import shop.maeum.domain.member.repository.MemberRepository
import shop.maeum.domain.security.util.SecurityUtil
import shop.maeum.global.dto.CursorPageResDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@Service
@Transactional(readOnly = true)
class DiaryService(
    private val diaryRepository: DiaryRepository,
    private val aiService: AiService,
    private val memberRepository: MemberRepository,
    private val securityUtil: SecurityUtil,

    @Value("\${ai.prompt.diary-analysis}")
    private val diaryAnalysisPrompt: String,
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

        val (emotionNames, feedbackTitle, feedbackContent) = parseAiResponse(aiRawResponse)

        val diary = Diary(
            title = writeDiaryReqDto.title,
            content = writeDiaryReqDto.content,
            privacySetting = writeDiaryReqDto.privacySetting,
            feedbackTitle = feedbackTitle,
            feedbackContent = feedbackContent,
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

    @Transactional
    fun writeDiaryWithDate(writeDiaryWithDateReqDto: WriteDiaryWithDateReqDto): WriteDiaryResDto {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with id ${securityUtil.getCurrentEmail()} not found")

        val fullPrompt = """
        $diaryAnalysisPrompt

        제목: ${writeDiaryWithDateReqDto.title}
        내용: ${writeDiaryWithDateReqDto.content}
    """.trimIndent()

        log.info("📜 최종 AI 프롬프트:\n$fullPrompt")

        val aiRawResponse = aiService.generate(fullPrompt)

        log.info("🧠 AI 응답 원문:\n$aiRawResponse")

        val (emotionNames, feedbackTitle, feedbackContent) = parseAiResponse(aiRawResponse)

        val diary = Diary(
            title = writeDiaryWithDateReqDto.title,
            content = writeDiaryWithDateReqDto.content,
            privacySetting = writeDiaryWithDateReqDto.privacySetting,
            feedbackTitle = feedbackTitle,
            feedbackContent = feedbackContent,
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

        val savedDiary = diaryRepository.save(diary)

        val targetDateTime = writeDiaryWithDateReqDto.createdAt.atStartOfDay()
        diaryRepository.updateCreatedAt(savedDiary.id!!, targetDateTime)

        val finalDiary = diaryRepository.findById(savedDiary.id!!).get()

        return WriteDiaryResDto.fromEntity(finalDiary)
    }

    private fun parseAiResponse(raw: String): Triple<List<String>, String, String> {
        val emotionRegex = Regex("""감정:\s*\[(.*?)\]""")
        val feedbackTitleRegex = Regex("""피드백 제목:\s*(.*?)\s*피드백 내용:""")
        val feedbackContentRegex = Regex("""피드백 내용:\s*(.*)""", RegexOption.DOT_MATCHES_ALL)

        val emotions = emotionRegex.find(raw)
            ?.groups?.get(1)
            ?.value
            ?.split(",")
            ?.map { it.trim() }
            ?: emptyList()

        val feedbackTitle = feedbackTitleRegex.find(raw)
            ?.groups?.get(1)
            ?.value
            ?.trim()
            ?: ""

        val feedbackContent = feedbackContentRegex.find(raw)
            ?.groups?.get(1)
            ?.value
            ?.trim()
            ?: ""

        return Triple(emotions, feedbackTitle, feedbackContent)
    }

    fun getDiaries(email: String, cursor: Long?, limit: Int = 3): CursorPageResDto<DiarySummaryResDto, Long> {
        val currentMember = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Current member not found")

        val isOwner = currentMember.email == email

        val diaries = if (isOwner) {
            diaryRepository.findAllByMemberWithCursor(
                memberId = currentMember.id!!,
                cursor = cursor,
                pageable = PageRequest.of(0, limit + 1)
            )
        } else {
            diaryRepository.findAllPublicByMemberEmailWithCursor(
                email = email,
                cursor = cursor,
                pageable = PageRequest.of(0, limit + 1)
            )
        }

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

        val kstZone = ZoneId.of("Asia/Seoul")

        return diaries.map { diary ->
            diary.createdAt!!
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(kstZone)
                .dayOfMonth
        }.distinct()
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

        log.info("Checking diary for memberId=${member.id}, between $start and $end")

        val diary = diaryRepository.findFirstByMemberIdAndCreatedAtBetween(member.id!!, start, end)

        log.info("Diary found? ${diary != null}, createdAt=${diary?.createdAt}")

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

    @Transactional
    fun togglePrivacySetting(diaryId: Long): PrivacySettingResDto {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member not found")

        val diary = diaryRepository.findByIdOrNull(diaryId)
            ?: throw DiaryNotFoundException()

        if (diary.member != member) {
            throw IllegalArgumentException("본인의 일기만 수정할 수 있습니다.")
        }

        diary.togglePrivacy()

        return PrivacySettingResDto.fromEntity(diary)
    }

    // 분석에 필요한 다이어리 정보 조회
    fun getWeeklyDiaries(member: Member, start: LocalDateTime, end: LocalDateTime): List<EmotionAnalysisDataDto> {

        val diaryEntityList = diaryRepository.findByMemberAndDateBetween(
            member = member,
            start = start,
            end = end,
            pageable = PageRequest.of(0, 7)
        )

        return diaryEntityList.map { diary ->
            EmotionAnalysisDataDto.fromDiary(diary)
        }
    }

}
