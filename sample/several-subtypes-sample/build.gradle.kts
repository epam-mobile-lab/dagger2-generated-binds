plugins {
    application
}

application {
    mainClassName = "com.epam.singlesubtype.Example"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(":dagger-binds-annotations"))
    annotationProcessor(project(":dagger-binds-processor"))

    implementation(Config.Libs.dagger)
    annotationProcessor(Config.Libs.daggerСompiler)
}
