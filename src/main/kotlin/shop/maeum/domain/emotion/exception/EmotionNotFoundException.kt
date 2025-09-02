package shop.maeum.domain.emotion.exception

import shop.maeum.global.error.exception.NotFoundGroupException

class EmotionNotFoundException : NotFoundGroupException("현재 일기에 존재하지 않는 감정입니다.")
