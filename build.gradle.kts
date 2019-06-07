plugins {
    java

    kotlin("jvm") version "1.3.31" apply false
    kotlin("kapt") version "1.3.31" apply false
    
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC14"
}

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
    }
}


allprojects {
    group = Config.group
    version = Config.version

    repositories {
        mavenCentral()
        jcenter()
        google()
    }
}
