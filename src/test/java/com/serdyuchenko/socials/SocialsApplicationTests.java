package com.serdyuchenko.socials;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
	"spring.ai.openai-sdk.api-key=test",
	"spring.autoconfigure.exclude="
		+ "org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration,"
		+ "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration"
})
class SocialsApplicationTests {
	@Test
	void contextLoads() {
	}

}
