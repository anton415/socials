package com.serdyuchenko.socials;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
	"spring.ai.openai-sdk.api-key=test"
})
class SocialsApplicationTests {
	@Test
	void contextLoads() {
	}
}
