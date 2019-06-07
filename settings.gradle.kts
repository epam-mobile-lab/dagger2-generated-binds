include(
    ":dagger-binds-annotations",
    ":dagger-binds-processor"
)

include(
    ":java-sample",
    ":several-subtypes"
)

project(":java-sample").projectDir = File(rootDir, "test-project/java-sample")
project(":several-subtypes").projectDir = File(rootDir, "test-project/several-subtypes")