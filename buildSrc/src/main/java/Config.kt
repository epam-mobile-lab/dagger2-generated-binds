object Config {
    const val group = "com.epam.daggerbinds"
    const val artifactId = "daggerbinds"
    const val version = "1.0.0-alpha1"

    object Libs {
        const val kotlinPoet = "com.squareup:kotlinpoet:1.2.0"
        const val javaPoet = "com.squareup:javapoet:1.11.1"

        private const val daggerVersion = "2.22.1"
        const val dagger = "com.google.dagger:dagger:$daggerVersion"
        const val daggerСompiler = "com.google.dagger:dagger-compiler:$daggerVersion"

        private const val ktlintVersion = "0.32.0"
        const val ktlint = "com.pinterest:ktlint:$ktlintVersion"

        private const val compileTestingVersion = "0.18"
        const val compileTesting = "com.google.testing.compile:compile-testing:$compileTestingVersion"

        private const val jUnitVersion = "4.12"
        const val jUnit = "junit:junit:$jUnitVersion"
    }
}