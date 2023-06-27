import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.1"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "com.epam.mentoring.kotlin"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")


	//----
	implementation("org.springframework:spring-context")
	implementation("org.springframework:spring-aop")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework:spring-web")
//	implementation("org.springframework:spring-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("mysql:mysql-connector-java:8.0.33")

	implementation("org.hibernate:hibernate-validator:8.0.1.Final")
//	implementation("org.springframework.boot:spring-boot-starter-security")
//	implementation("org.springframework.security:spring-security-config:6.1.1")
	implementation("io.jsonwebtoken:jjwt:0.9.1")

	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-cache:3.1.1")
	//pictures
	compileOnly("com.google.android:android:4.0.1.2")


		//coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
	runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.1")


	//----

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
