package org.usmanzaheer1995.springbootsecurity

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.with
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestSpringBootSecurityApplication {

	@Bean
	@ServiceConnection
	fun postgresContainer(): PostgreSQLContainer<*> {
		return PostgreSQLContainer(DockerImageName.parse("postgres:16"))
	}

}

fun main(args: Array<String>) {
	fromApplication<SpringBootSecurityApplication>().with(TestSpringBootSecurityApplication::class).run(*args)
}
