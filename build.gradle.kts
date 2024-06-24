import nu.studer.gradle.jooq.JooqGenerate
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.util.Properties

val packageName = "org.usmanzaheer1995.springbootsecurity"

val env = System.getenv("DEMO_ENV") ?: "local"
val propertiesFileName = "application-$env.properties"
val properties = Properties()
project.file("src/main/resources/$propertiesFileName").inputStream().use { properties.load(it) }

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.graalvm.buildtools.native") version "0.9.28"
    id("org.flywaydb.flyway") version "10.1.0"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
    id("nu.studer.jooq") version "9.0"
    id("org.openapi.generator") version "5.3.0"
}

group = "org.usmanzaheer1995"
version = "0.0.1-SNAPSHOT"

ext["jooq.version"] = jooq.version.get()

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

graalvmNative {
    testSupport = false
}

buildscript {
    dependencies {
        classpath("org.springdoc:springdoc-openapi-starter-common:2.3.0")
        classpath("org.flywaydb:flyway-core:10.6.0")
        classpath("org.flywaydb:flyway-database-postgresql:10.6.0")
        classpath("org.testcontainers:postgresql:1.19.3")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jdbc:3.2.4") {
        exclude(group = "org.springframework", module = "spring-core")
    }
    implementation("org.springframework:spring-core:6.1.3")
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.4")
    implementation("org.springframework.boot:spring-boot-starter-security:3.2.4")
    developmentOnly("org.springframework.boot:spring-boot-devtools:3.2.2")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose:3.2.2")
    testImplementation("org.springframework.security:spring-security-test:6.2.4")
    testImplementation("org.springframework.boot:spring-boot-testcontainers:3.2.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.4") {
        exclude(group = "com.jayway.jsonpath", module = "json-path")
        exclude(module = "mockito-core")
    }
    testImplementation("com.ninja-squad:springmockk:4.0.2")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
    implementation("org.springdoc:springdoc-openapi-starter-common:2.3.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.jooq:jooq:3.19.3")
    implementation("org.flywaydb:flyway-core:10.6.0")
    implementation("org.flywaydb:flyway-database-postgresql:10.6.0")

    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.5")

    jooqGenerator("org.postgresql:postgresql:42.5.5")
    runtimeOnly("org.postgresql:postgresql:42.5.5")

    testImplementation("com.jayway.jsonpath:json-path:2.9.0")
    testImplementation("org.testcontainers:junit-jupiter:1.19.4")
    testImplementation("org.testcontainers:postgresql:1.19.3")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val dbUrl: String = properties.getProperty("spring.datasource.url")
val dbUser: String = properties.getProperty("spring.datasource.username")
val dbPassword: String = properties.getProperty("spring.datasource.password")

val flywayDbUrl: String = properties.getProperty("spring.flyway.url")
val flywayDbUser: String = properties.getProperty("spring.flyway.user")
val flywayDbPassword: String = properties.getProperty("spring.flyway.password")

val containerInstance: PostgreSQLContainer<Nothing>? =
    if ("generateJooq" in project.gradle.startParameter.taskNames) {
        PostgreSQLContainer<Nothing>(
            DockerImageName.parse(
                "postgres:16-alpine",
            ),
        ).apply {
            withDatabaseName("mydatabase")
            start()
        }
    } else {
        null
    }

flyway {
    url = containerInstance?.jdbcUrl ?: flywayDbUrl
    user = containerInstance?.username ?: flywayDbUser
    password = containerInstance?.password ?: flywayDbPassword
    driver = "org.postgresql.Driver"
    schemas = arrayOf("public")
    locations = arrayOf("filesystem:${project.projectDir}/src/main/resources/db/migration")
}

jooq {
    configurations {
        create("") {
            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = containerInstance?.jdbcUrl
                    user = containerInstance?.username
                    password = containerInstance?.password
                    properties.add(
                        Property().apply {
                            key = "ssl"
                            value = "false"
                        },
                    )
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        isIncludeIndexes = false
                        excludes = "flyway.*"
                    }
                    generate.apply {
                        isRelations = true
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "$packageName.persistence.db"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.named<JooqGenerate>("generateJooq") {
    (launcher::set)(
        javaToolchains.launcherFor {
            dependsOn(tasks.named("flywayMigrate"))
            languageVersion.set(JavaLanguageVersion.of(21))
            doLast {
                containerInstance?.stop()
            }
        },
    )
}

val oasPackage = "$packageName.openapi"
val oasSpecLocation = "api-definition.json"
val oasGenOutputDir = project.layout.buildDirectory.dir("generated-oas")

openApiGenerate {
    inputSpec = project.file("${project.rootDir}/$oasSpecLocation").path
    generatorName = "kotlin-spring"
    outputDir = project.file(oasGenOutputDir).path
    apiPackage = "$oasPackage.api"
    modelPackage = "$oasPackage.model"
    configOptions =
        mapOf(
            "dateLibrary" to "java21",
            "interfaceOnly" to "true",
            "useTags" to "true",
        )
}

tasks.bootJar {
    archiveFileName.set("spring-boot-security-$env.jar")
    enabled = true
}

sourceSets {
    main {
        kotlin {
            srcDir("build/generated-src/jooq")
            srcDir("build/generated-oas/src/main/kotlin")
        }
    }
}
