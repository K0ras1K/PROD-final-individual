val exposed_version: String by project
val hikaricp_version: String by project
val kotlin_redis_version: String by project

plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "online.k0ras1k"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // TELEGRAM-BOAT-API
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.1.0")
    // DOTENV
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    // LOGGING
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("ch.qos.logback:logback-core:1.4.14")
    implementation("org.slf4j:slf4j-api:2.0.10")
    implementation("org.slf4j:slf4j-simple:2.0.10")
    // REDIS
    implementation("redis.clients:jedis:5.0.0")
    // DATABASE
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("org.postgresql:postgresql:42.7.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "online.k0ras1k.travelagent.MainKt"
    }
}

kotlin {
    jvmToolchain(17)
}
