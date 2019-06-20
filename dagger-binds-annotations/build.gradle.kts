plugins {
    `java-library`
}

ext.set("bintrayName", "generated-binds-annotation")

apply {
    from("../gradle/script/publish.gradle")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
