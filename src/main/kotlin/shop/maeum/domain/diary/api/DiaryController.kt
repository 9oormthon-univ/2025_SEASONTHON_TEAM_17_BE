package shop.maeum.domain.diary.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import shop.maeum.domain.diary.api.dto.request.WriteDiaryReqDto
import shop.maeum.domain.diary.api.dto.response.DiaryDetailResDto
import shop.maeum.domain.diary.api.dto.response.DiarySummaryResDto
import shop.maeum.domain.diary.api.dto.response.WriteDiaryResDto
import shop.maeum.domain.diary.application.DiaryService
import shop.maeum.global.template.RspTemplate

@RestController
@RequestMapping("/api/v1/diaries")
class DiaryController(
    private val diaryService: DiaryService
) : DiaryDocs {

    @PostMapping
    override fun writeDiary(@RequestBody writeDiaryReqDto: WriteDiaryReqDto): RspTemplate<WriteDiaryResDto> {
        return RspTemplate(
            httpStatus = HttpStatus.CREATED,
            message = "일기가 성공적으로 작성되었습니다.",
            data = diaryService.writeDiary(
                writeDiaryReqDto
            )
        )
    }

//    @GetMapping
//    fun getMyDiaries(
//    ): RspTemplate<List<DiarySummaryResDto>> {
//        val diaries = diaryService.getDiaries(멤버 보내기)
//        return RspTemplate(
//            httpStatus = HttpStatus.OK,
//            message = "내 일기 리스트 조회 성공",
//            data = diaries
//        )
//    }

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
}
