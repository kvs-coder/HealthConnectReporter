import kotlinx.kover.gradle.plugin.dsl.CoverageUnit

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)
    `maven-publish`
}

android {
    namespace = "com.kvs.healthconnectreporter"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

kotlin {
    explicitApi()
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    // implementation: Health Connect types never appear in the public API (library.api).
    implementation(libs.health.connect.client)
    // api: Flow is part of the public API (HealthConnectObserver.observe).
    api(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}

kover {
    reports {
        filters {
            excludes {
                classes("*.BuildConfig")
            }
        }
        verify {
            rule {
                // Raise both to the measured level with every test PR; never lower them. Every missed branch is
                // kotlinx.serialization-generated code on @Serializable lines (write${'$'}Self, deserialization
                // constructors), unreachable from the library's encodeDefaults JSON.
                minBound(100, CoverageUnit.LINE)
                minBound(74, CoverageUnit.BRANCH)
            }
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = providers.gradleProperty("GROUP").get()
                artifactId = providers.gradleProperty("POM_ARTIFACT_ID").get()
                version = providers.gradleProperty("VERSION_NAME").get()
            }
        }
    }
}
