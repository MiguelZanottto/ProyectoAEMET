plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    annotationProcessor ("org.projectlombok:lombok:1.18.30")
    implementation("org.projectlombok:lombok:1.18.28")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.opencsv:opencsv:5.8")
    implementation("org.xerial:sqlite-jdbc:3.43.0.0")
    implementation("org.mybatis:mybatis:3.5.13")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

tasks.test {
    useJUnitPlatform()
}