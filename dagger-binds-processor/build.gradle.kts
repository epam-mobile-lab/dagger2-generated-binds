import org.gradle.internal.jvm.Jvm
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

ext.set("bintrayName", "generated-binds-processor")
ext.set("bintrayDesc", "Annotation processor which generates dagger binds modules.")

plugins {
    `java-library`
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}

apply {
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
    testApi(Config.Libs.compileTesting)
    testApi(Config.Libs.jUnit)
    testApi(files(Jvm.current().toolsJar))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
