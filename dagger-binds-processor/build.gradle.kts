import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}

apply{
    from("../gradle/script/ktlint.gradle")
    from("../gradle/script/detekt.gradle")
    from("../gradle/script/publish.gradle")
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    api(Config.Libs.javaPoet)
    api(Config.Libs.dagger)
    api(project(":dagger-binds-annotations"))
    "ktlint"(Config.Libs.ktlint)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
