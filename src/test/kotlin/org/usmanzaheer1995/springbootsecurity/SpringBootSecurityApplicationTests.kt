package org.usmanzaheer1995.springbootsecurity

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource

@Import(TestSpringBootSecurityApplication::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["spring.datasource.url=jdbc:tc:postgresql:16:///mydatabase"])
class SpringBootSecurityApplicationTests {

	@Test
	fun contextLoads() {
	}

}
