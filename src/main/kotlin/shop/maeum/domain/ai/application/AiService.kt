package shop.maeum.domain.ai.application

import org.springframework.ai.chat.ChatClient
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.stereotype.Service

@Service
class AiService(
    private val chatClient: ChatClient
) {
    fun generate(prompt: String): String {
        val response = chatClient.call(
            Prompt(
                prompt,
                OpenAiChatOptions.builder()
                    .withModel("gpt-4o")
                    .withTemperature(0.8f)
                    .withMaxTokens(512)
                    .build()
            )
        )

        return response.result.output.content.trim()
    }
}
