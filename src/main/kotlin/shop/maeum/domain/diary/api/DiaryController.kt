package shop.maeum.domain.diary.api

import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import shop.maeum.domain.diary.api.dto.request.WriteDiaryReqDto
import shop.maeum.domain.diary.api.dto.response.DiaryDetailResDto
import shop.maeum.domain.diary.api.dto.response.DiaryListWithPageResDto
import shop.maeum.domain.diary.api.dto.response.WriteDiaryResDto
import shop.maeum.domain.diary.application.DiaryService
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
        @RequestParam(name = "page", defaultValue = "0") page: Int,
        @RequestParam(name = "size", defaultValue = "10") size: Int
    ): RspTemplate<DiaryListWithPageResDto> {
        val pageable = PageRequest.of(page, size)
        val diaries = diaryService.getDiaries(pageable)
        return RspTemplate(
            HttpStatus.OK,
            message = "일기 목록 조회 성공",
            data = diaries
        )
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
    fun deleteDiary(
        @PathVariable diaryId: Long
    ): RspTemplate<Void> {
        diaryService.deleteDiary(diaryId)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "일기가 성공적으로 삭제되었습니다."
        )
    }

}
