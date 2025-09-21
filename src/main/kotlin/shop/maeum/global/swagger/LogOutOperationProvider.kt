package shop.maeum.global.swagger

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import shop.maeum.domain.security.dto.LogOutRequest

@Component
class LogOutOperationProvider {

    fun getLogOutOperation(): Operation {
        val requestBody = RequestBody()
            .description("로그아웃 요청 형식")
            .required(true)
            .content(Content().apply {
                addMediaType(
                    MediaType.APPLICATION_JSON_VALUE,
                    io.swagger.v3.oas.models.media.MediaType().schema(Schema<LogOutRequest>())
                        .example(mapOf(
                            "accessToken" to "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                            "refreshToken" to "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                        )
                )
            })

        return Operation()
            .summary("로그아웃")
            .description("사용자 로그아웃 처리")
            .addTagsItem("OAuth")
            .requestBody(requestBody)
            .responses(ApiResponses().apply {
                addApiResponse("200", ApiResponse()
                    .description("로그아웃 정상 처리")
                    .content(Content().apply {
                        addMediaType(
                            MediaType.APPLICATION_JSON_VALUE,
                            io.swagger.v3.oas.models.media.MediaType()
                                .example(
                                    mapOf(
                                        "statusCode" to 200,
                                        "message" to "로그아웃이 완료되었습니다."
                                    )
                                )
                        )
                    })
                )


            })
    }
}