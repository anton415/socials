package com.serdyuchenko.socials;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
	"spring.ai.openai-sdk.api-key=test",
	"spring.liquibase.enabled=false",
	"spring.autoconfigure.exclude=org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration"
})
class SocialsApplicationTests {
	@Test
	void contextLoads() {
	}
}
