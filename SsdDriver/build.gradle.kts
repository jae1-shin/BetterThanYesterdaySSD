plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.mockito:mockito-junit-jupiter:5.18.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17 // JDK 버전에 맞게 설정
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("Ssd") // 🔁 여기를 실제 메인 클래스 이름으로 바꾸세요
}


tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }

    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveFileName.set("ssd.jar") // 원하는 jar 이름
}

