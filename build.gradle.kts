import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
//    id("org.springframework.boot") version "3.1.1"
    id("org.springframework.boot") version "2.7.0"
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
//    implementation("org.springframework.boot:spring-boot-starter:2.7.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")


    //----
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-aop")

    implementation("org.springframework.boot:spring-boot-starter-webflux")


//    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.asyncer:r2dbc-mysql:1.0.2")

    implementation("org.hibernate:hibernate-validator:8.0.1.Final")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    implementation("org.springframework.boot:spring-boot-starter-cache:3.1.1")



    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.1")


    implementation("io.springfox:springfox-boot-starter:3.0.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("io.mockk:mockk:1.13.5")
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
