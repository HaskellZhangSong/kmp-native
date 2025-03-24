plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// ./System.setProperty("kotlin.native.home", "/Users/songzh/project/github/kotlin/kotlin-native/dist")
System.setProperty("konan.home", "/Users/songzh/project/github/kotlin/kotlin-native/dist")
System.setProperty("KOTLIN_NATIVE_HOME", "/Users/songzh/project/github/kotlin/kotlin-native/dist")

tasks.withType<Exec>().configureEach {
    doFirst {
        println("Executing command: ${commandLine.joinToString(" ")}")
    }
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isArm64 = System.getProperty("os.arch") == "aarch64"
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" && isArm64 -> macosArm64("native")
        hostOs == "Mac OS X" && !isArm64 -> macosX64("native")
        hostOs == "Linux" && isArm64 -> linuxArm64("native")
        hostOs == "Linux" && !isArm64 -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    val targets = nativeTarget.apply {
        binaries {
            staticLib {
                baseName = "Lib1"
                freeCompilerArgs += listOf("-p", "taihe_static")
            }
//
//            executable {
//                entryPoint = "main"
//            }
        }
    }
    targets

//    sourceSets {
//        nativeMain.dependencies {
//            implementation(libs.kotlinxSerializationJson)
//        }
//    }
}
