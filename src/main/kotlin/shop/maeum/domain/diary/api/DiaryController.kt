package shop.maeum.domain.diary.api

import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import shop.maeum.domain.diary.api.dto.request.WriteDiaryReqDto
import shop.maeum.domain.diary.api.dto.response.*
import shop.maeum.domain.diary.application.DiaryService
import shop.maeum.global.dto.CursorPageResDto
import shop.maeum.global.template.RspTemplate

@RestController
@RequestMapping("/api/v1/diaries")
class DiaryController(
    private val diaryService: DiaryService
) : DiaryDocs {

    @PostMapping
    override fun writeDiary(
        @RequestBody writeDiaryReqDto: WriteDiaryReqDto
    ): RspTemplate<WriteDiaryResDto> {
        return RspTemplate(
            httpStatus = HttpStatus.CREATED,
            message = "일기가 성공적으로 작성되었습니다.",
            data = diaryService.writeDiary(
                writeDiaryReqDto,
            )
        )
    }

    @GetMapping
    override fun getDiaries(
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "3") limit: Int
    ): RspTemplate<CursorPageResDto<DiarySummaryResDto, Long>> {
        val diaries = diaryService.getDiaries(cursor, limit)
        return RspTemplate(HttpStatus.OK, "일기 목록 조회 성공", diaries)
    }

    @GetMapping("/{diaryId}")
    override fun getDiaryDetail(
        @PathVariable diaryId: Long
    ): RspTemplate<DiaryDetailResDto> {
        val diaryDetail = diaryService.getDiaryDetail(diaryId)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "일기 상세 조회 성공",
            data = diaryDetail
        )
    }

    @DeleteMapping("/{diaryId}")
    override fun deleteDiary(
        @PathVariable diaryId: Long
    ): RspTemplate<Void> {
        diaryService.deleteDiary(diaryId)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "일기가 성공적으로 삭제되었습니다."
        )
    }

    @GetMapping("/month")
    override fun getWrittenDaysInMonth(
        @RequestParam year: Int,
        @RequestParam month: Int
    ): RspTemplate<List<Int>> {
        val days = diaryService.getWrittenDaysInMonth(year, month)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "해당 월의 작성된 일기 날짜 목록 조회 성공",
            data = days
        )
    }

    @GetMapping("/date")
    override fun getDiaryByDate(
        @RequestParam year: Int,
        @RequestParam month: Int,
        @RequestParam day: Int
    ): RspTemplate<DiaryDetailResDto> {
        val diary = diaryService.getDiaryByDate(year, month, day)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "특정 날짜의 일기 조회 성공",
            data = diary
        )
    }

    @GetMapping("/today")
    override fun hasDiaryToday(): RspTemplate<DiaryTodayResDto> {
        val result = diaryService.hasDiaryToday()
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = if (result.written) "오늘 작성한 일기가 있습니다." else "오늘 작성한 일기가 없습니다.",
            data = result
        )
    }
}
