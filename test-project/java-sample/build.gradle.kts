plugins {
    application
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClassName = "com.epam.sample.Example"
}

dependencies {
    implementation(project(":dagger-binds-annotations"))
    annotationProcessor(project(":dagger-binds-processor"))

    implementation(Config.Libs.dagger)
    annotationProcessor(Config.Libs.daggerСompiler)
}
