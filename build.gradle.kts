plugins {
    java
}

group = "com.something"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testCompile("org.junit.jupiter", "junit-jupiter-engine", "5.2.0")
    testCompile("org.hamcrest", "hamcrest-all", "1.3")

    compile("com.google.guava", "guava", "25.1-jre")
    compile("org.hibernate", "hibernate-core", "5.3.4.Final")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_9
}