plugins {
    `java-library`
}

apply{
    from("../gradle/script/publish.gradle")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
