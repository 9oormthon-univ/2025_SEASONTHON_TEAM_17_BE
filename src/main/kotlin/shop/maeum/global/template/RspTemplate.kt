package shop.maeum.global.template

import org.springframework.http.HttpStatus

// 응답 템플릿
class RspTemplate<T> {
    var statusCode: Int
    var message: String
    var data: T? = null

    constructor(httpStatus: HttpStatus, message: String, data: T) {
        this.statusCode = httpStatus.value()
        this.message = message
        this.data = data
    }

    constructor(httpStatus: HttpStatus, message: String) {
        this.statusCode = httpStatus.value()
        this.message = message
    }
}