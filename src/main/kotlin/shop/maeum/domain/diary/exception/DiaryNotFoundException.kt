package shop.maeum.domain.diary.exception

import shop.maeum.global.error.exception.NotFoundGroupException

class DiaryNotFoundException : NotFoundGroupException("존재하지 않는 일기입니다.")
