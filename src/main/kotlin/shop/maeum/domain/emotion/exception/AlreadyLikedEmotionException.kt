package shop.maeum.domain.emotion.exception

import shop.maeum.global.error.exception.InvalidGroupException

class AlreadyLikedEmotionException : InvalidGroupException("이미 좋아요를 누른 감정입니다.")
