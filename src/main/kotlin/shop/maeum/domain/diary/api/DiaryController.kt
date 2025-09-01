package shop.maeum.domain.diary.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import shop.maeum.domain.diary.api.dto.request.WriteDiaryReqDto
import shop.maeum.domain.diary.api.dto.response.WriteDiaryResDto
import shop.maeum.domain.diary.application.DiaryService
import shop.maeum.global.template.RspTemplate

@RestController
@RequestMapping("/api/v1/diaries")
class DiaryController(
    private val diaryService: DiaryService
) {

    @PostMapping
    fun writeDiary(@RequestBody writeDiaryResDto: WriteDiaryReqDto): RspTemplate<WriteDiaryResDto> {
        return RspTemplate(
            httpStatus = HttpStatus.CREATED,
            message = "일기가 성공적으로 작성되었습니다.",
            data = diaryService.writeDiary(
                writeDiaryResDto
            )
        )
    }
}
