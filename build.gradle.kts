plugins {
    java
}

group = "com.something"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

buildscript {
    dependencies {
        classpath("io.ebean:ebean-gradle-plugin:11.5.3")
    }
}

dependencies {
    testCompile("org.junit.jupiter", "junit-jupiter-engine", "5.2.0")
    testCompile("org.hamcrest", "hamcrest-all", "1.3")

    compile("com.google.guava", "guava", "25.1-jre")
    compile("com.h2database", "h2", "1.4.197")
    compile("org.springframework.data", "spring-data-jpa", "2.0.9.RELEASE")
    compile("org.slf4j", "slf4j-simple", "1.7.25")
    compile("org.jetbrains", "annotations", "16.0.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_10
}