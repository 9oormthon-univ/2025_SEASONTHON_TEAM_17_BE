package shop.maeum.global.error

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import shop.maeum.global.error.dto.ErrorResponse
import shop.maeum.global.error.exception.AccessDeniedGroupException
import shop.maeum.global.error.exception.AuthGroupException
import shop.maeum.global.error.exception.InvalidGroupException
import shop.maeum.global.error.exception.NotFoundGroupException

@RestControllerAdvice
class ControllerAdvice {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(InvalidGroupException::class)
    fun handleInvalidData(e: RuntimeException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.message ?: "Invalid request")
        log.error(e.message, e)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AuthGroupException::class)
    fun handleAuthData(e: RuntimeException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.message ?: "Auth error")
        log.error(e.message, e)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NotFoundGroupException::class)
    fun handleNotFoundData(e: RuntimeException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.NOT_FOUND.value(), e.message ?: "Not found")
        log.error(e.message, e)
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(AccessDeniedGroupException::class)
    fun handleAccessDeniedData(e: RuntimeException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.FORBIDDEN.value(), e.message ?: "Access denied")
        log.error(e.message, e)
        return ResponseEntity(errorResponse, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val fieldError: FieldError = e.fieldError
            ?: return ResponseEntity(
                ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation error"),
                HttpStatus.BAD_REQUEST
            )

        val errorResponse = ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "${fieldError.defaultMessage}. (${fieldError.field})"
        )

        log.error("Validation error for field {}: {}", fieldError.field, fieldError.defaultMessage)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handBaseException(e: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            e.message ?: "서버에서 발생한 오류 입니다."
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
