package shop.maeum.domain.diary.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shop.maeum.domain.diary.api.dto.request.WriteDiaryReqDto
import shop.maeum.domain.diary.api.dto.response.WriteDiaryResDto
import shop.maeum.domain.diary.domain.Diary
import shop.maeum.domain.diary.domain.repository.DiaryRepository
import shop.maeum.global.entity.Status

@Service
@Transactional(readOnly = true)
class DiaryService(
    private val diaryRepository: DiaryRepository
) {

    @Transactional
    fun writeDiary(
        writeDiaryReqDto: WriteDiaryReqDto
    ): WriteDiaryResDto {
        val diary = Diary(
            title = writeDiaryReqDto.title,
            content = writeDiaryReqDto.content,
            privacySetting = writeDiaryReqDto.privacySetting,
            feedback = "", // 처음에는 비어 있음
            status = Status.ACTIVE,
        )
        diaryRepository.save(diary)

        return WriteDiaryResDto.fromEntity(diary)
    }
}
