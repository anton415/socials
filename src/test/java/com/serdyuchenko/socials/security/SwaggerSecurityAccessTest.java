package com.serdyuchenko.socials.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Проверяет, что Swagger и OpenAPI доступны без JWT-токена.
 */
@SpringBootTest(properties = {
    "spring.ai.openai-sdk.api-key=test"
})
@AutoConfigureMockMvc
class SwaggerSecurityAccessTest {
    @Autowired
    private MockMvc mockMvc;

    /**
     * Проверяет, что Swagger UI отдается без авторизации.
     *
     * @throws Exception если выполнение HTTP-запроса завершилось ошибкой
     */
    @Test
    void whenRequestSwaggerUiWithoutTokenThenOk() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    /**
     * Проверяет, что спецификация OpenAPI доступна без авторизации.
     *
     * @throws Exception если выполнение HTTP-запроса завершилось ошибкой
     */
    @Test
    void whenRequestOpenApiDocsWithoutTokenThenOk() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }
}
