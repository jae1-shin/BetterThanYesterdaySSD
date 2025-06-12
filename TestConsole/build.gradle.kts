plugins {
    id("java")
    id("application")  // 이 줄이 꼭 있어야 합니다!
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("Main") // 🔁 여기를 실제 메인 클래스 이름으로 바꾸세요
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.mockito:mockito-junit-jupiter:5.18.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }

    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

    destinationDirectory = file("../JarLibs")

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveFileName.set("shell.jar") // 원하는 jar 이름
}
