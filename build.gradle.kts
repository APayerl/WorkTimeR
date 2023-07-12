plugins {
    application
}

group = "se.payerl"
version = "1.0-SNAPSHOT"

repositories {
    maven {url = uri("https://jitpack.io") }
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
    implementation("org.jetbrains:annotations:24.0.1")

}

application {
    mainClass.set("se.payerl.Main")
}

//jar {
//    manifest {
//        attributes 'Main-Class': application.mainClass
//    }
//}

tasks.test {
    useJUnitPlatform()
}

//tasks.withType<KotlinCompile> {
//    kotlinOptions.jvmTarget = "17"
//}
