include(
    ":dagger-binds-annotations",
    ":dagger-binds-processor"
)

include(
    ":single-subtype-sample",
    ":several-subtypes-sample"
)

project(":single-subtype-sample").projectDir = File(rootDir, "sample/single-subtype-sample")
project(":several-subtypes-sample").projectDir = File(rootDir, "sample/several-subtypes-sample")