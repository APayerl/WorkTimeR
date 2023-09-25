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

tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "processResources")) // We need this for Gradle optimization to work
        archiveClassifier.set("standalone") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
    test {
        useJUnitPlatform()
    }
}

//jar {
//    manifest {
//        attributes 'Main-Class': application.mainClass
//    }
//}

//tasks.withType<KotlinCompile> {
//    kotlinOptions.jvmTarget = "17"
//}
