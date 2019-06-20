plugins {
    `java-library`
}

ext.set("bintrayName", "generated-binds-annotation")
ext.set("bintrayDesc", "Annotations set for dagger2-generated-binds processor.")

apply {
    from("../gradle/script/publish.gradle")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
