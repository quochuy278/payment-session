import org.yaml.snakeyaml.Yaml

buildscript {
	repositories {
		mavenCentral()
	}
	dependencies {
		classpath("org.yaml:snakeyaml:2.6")
	}
}

plugins {
	kotlin("jvm") version "2.3.21"
	kotlin("plugin.spring") version "2.3.21"
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.jooq.jooq-codegen-gradle") version "3.21.5"
}

group = "io.github.quochuy278"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-flyway")
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.1.0")
	implementation("tools.jackson.module:jackson-module-kotlin")
	jooqCodegen("org.postgresql:postgresql")
	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

val localDatasource: Map<String, String> = file("config/application.yml")
	.takeIf { it.exists() }
	?.reader()
	?.use { reader ->
		val application = Yaml().load<Map<String, Any>>(reader)
		@Suppress("UNCHECKED_CAST")
		val spring = application["spring"] as? Map<String, Any> ?: emptyMap()
		@Suppress("UNCHECKED_CAST")
		val datasource = spring["datasource"] as? Map<String, Any> ?: emptyMap()
		datasource.mapValues { (_, value) -> value.toString() }
	}
	?: emptyMap()

val databaseUrl = providers.environmentVariable("DATABASE_URL")
	.orElse(providers.provider { localDatasource["url"] })
val databaseUsername = providers.environmentVariable("DATABASE_USERNAME")
	.orElse(providers.provider { localDatasource["username"] })
val databasePassword = providers.environmentVariable("DATABASE_PASSWORD")
	.orElse(providers.provider { localDatasource["password"] })

jooq {
	configuration {
		jdbc {
			driver = "org.postgresql.Driver"
			url = databaseUrl.get()
			user = databaseUsername.get()
			password = databasePassword.get()
		}

		generator {
			name = "org.jooq.codegen.KotlinGenerator"

			database {
				name = "org.jooq.meta.postgres.PostgresDatabase"
				inputSchema = "payment"
			}

			generate {
				isPojos = false
				isDaos = false
				isRecords = true
			}

			target {
				packageName = "io.github.quochuy278.paymentsession.persistence.jooq.generated"
				directory = layout.buildDirectory
					.dir("generated-src/jooq/main")
					.get()
					.asFile
					.absolutePath
			}
		}
	}
}

tasks.named("jooqCodegen") {
	// PostgreSQL is external state that Gradle cannot fingerprint reliably.
	outputs.upToDateWhen { false }
	inputs.files(fileTree("src/main/resources/db/migration"))
}

tasks.named("compileKotlin") {
	dependsOn(tasks.named("jooqCodegen"))
}

tasks.withType<Test> {
	useJUnitPlatform()
}
