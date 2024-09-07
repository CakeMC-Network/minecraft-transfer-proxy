import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.nio.charset.StandardCharsets
import java.net.URI
import java.util.Properties

plugins {
    id("idea")

    id("java")
    id("java-library")

    id("com.github.johnrengelman.shadow") version "8.1.1"

  id("maven-publish")
}

group = "net.cakemc"
version = "0.0.0-develop"

val repoProperties = Properties()
val credFile = file("credentials.properties")
if (credFile.exists()) {
    repoProperties.load(file("credentials.properties").inputStream())   
}
val repoUsername = repoProperties.getProperty("username", System.getenv("REPOSITORY_USERNAME"))
val repoPassword = repoProperties.getProperty("password", System.getenv("REPOSITORY_PASSWORD"))

repositories {
    mavenCentral()
    mavenLocal()
    maven {
      name = "cakemc"
      url = URI.create("http://cakemc.net:8081/repository/maven-releases")
      credentials {
        username = repoUsername
        password = repoPassword
      }
      isAllowInsecureProtocol = true
    }
}

fun <V> prop(value: String): V {
    return properties.getValue(value) as V
}

publishing {
  publications.create<MavenPublication>(rootProject.name) {
    artifact(tasks.shadowJar)
  }
  repositories {
    maven {
      name = "cakemc"
      url = URI.create("http://cakemc.net:8081/repository/maven-releases")
      credentials {
        username = repoUsername
        password = repoPassword
      }
      isAllowInsecureProtocol = true
    }
  }
}

dependencies {
    implementation("net.cakemc:library:0.0.0-develop:all")
    implementation("net.cakemc:protocol:0.0.0-develop:all")

    annotationProcessor(
        group = "org.jetbrains",
        name = "annotations",
        version = prop("dep_ann-jbr")
    )
    implementation(
        group = "org.jetbrains",
        name = "annotations",
        version = prop("dep_ann-jbr")
    )


    annotationProcessor(
        group = "com.fasterxml.jackson.core",
        name = "jackson-annotations",
        version = prop("dep-jackson")
    );
    implementation(
        group = "com.fasterxml.jackson.core",
        name = "jackson-annotations",
        version = prop("dep-jackson")
    );
    shadow(
        group = "com.fasterxml.jackson.core",
        name = "jackson-annotations",
        version = prop("dep-jackson")
    );

    implementation(
        group = "com.fasterxml.jackson.core",
        name = "jackson-core",
        version = prop("dep-jackson")
    );
    shadow(
        group = "com.fasterxml.jackson.core",
        name = "jackson-core",
        version = prop("dep-jackson")
    );

    implementation(
        group = "com.fasterxml.jackson.core",
        name = "jackson-databind",
        version = prop("dep-jackson")
    );
    shadow(
        group = "com.fasterxml.jackson.core",
        name = "jackson-databind",
        version = prop("dep-jackson")
    );

    implementation(
        group = "io.github.speiger",
        name = "Primitive-Collections",
        version = prop("dep-reinspeigern")
    )
    shadow(
        group = "io.github.speiger",
        name = "Primitive-Collections",
        version = prop("dep-reinspeigern")
    )

    implementation(
        group = "io.netty",
        name = "netty-common",
        version = prop("dep-netty")
    )
    shadow(
        group = "io.netty",
        name = "netty-common",
        version = prop("dep-netty")
    )

    implementation(
        group = "io.netty",
        name = "netty-buffer",
        version = prop("dep-netty")
    )
    shadow(
        group = "io.netty",
        name = "netty-buffer",
        version = prop("dep-netty")
    )

    implementation(
        group = "io.netty",
        name = "netty-codec",
        version = prop("dep-netty")
    )
    shadow(
        group = "io.netty",
        name = "netty-codec",
        version = prop("dep-netty")
    )

    implementation(
        group = "io.netty",
        name = "netty-resolver",
        version = prop("dep-netty")
    )
    shadow(
        group = "io.netty",
        name = "netty-resolver",
        version = prop("dep-netty")
    )

    implementation(
        group = "io.netty",
        name = "netty-codec",
        version = prop("dep-netty")
    )
    shadow(
        group = "io.netty",
        name = "netty-codec",
        version = prop("dep-netty")
    )

    implementation(
        group = "io.netty",
        name = "netty-transport",
        version = prop("dep-netty")
    )
    shadow(
        group = "io.netty",
        name = "netty-transport",
        version = prop("dep-netty")
    )

    implementation(
        group = "io.netty",
        name = "netty-transport-classes-epoll",
        version = prop("dep-netty")
    )
    shadow(
        group = "io.netty",
        name = "netty-transport-classes-epoll",
        version = prop("dep-netty")
    )

    implementation(
        group = "io.netty",
        name = "netty-transport-classes-kqueue",
        version = prop("dep-netty")
    )
    shadow(
        group = "io.netty",
        name = "netty-transport-classes-kqueue",
        version = prop("dep-netty")
    )

    implementation(
        group = "io.netty",
        name = "netty-transport-native-epoll",
        version = prop("dep-netty"),
        classifier = "linux-aarch_64"
    )
    shadow(
        group = "io.netty",
        name = "netty-transport-native-epoll",
        version = prop("dep-netty"),
        classifier = "linux-aarch_64"
    )

    implementation(
        group = "io.netty",
        name = "netty-transport-native-epoll",
        version = prop("dep-netty"),
        classifier = "linux-riscv64"
    )
    shadow(
        group = "io.netty",
        name = "netty-transport-native-epoll",
        version = prop("dep-netty"),
        classifier = "linux-riscv64"
    )

    implementation(
        group = "io.netty",
        name = "netty-transport-native-epoll",
        version = prop("dep-netty"),
        classifier = "linux-x86_64"
    )
    shadow(
        group = "io.netty",
        name = "netty-transport-native-epoll",
        version = prop("dep-netty"),
        classifier = "linux-x86_64"
    )

    implementation(
        group = "io.netty",
        name = "netty-transport-native-kqueue",
        version = prop("dep-netty"),
        classifier = "osx-aarch_64"
    )
    shadow(
        group = "io.netty",
        name = "netty-transport-native-kqueue",
        version = prop("dep-netty"),
        classifier = "osx-aarch_64"
    )

    implementation(
        group = "io.netty",
        name = "netty-transport-native-kqueue",
        version = prop("dep-netty"),
        classifier = "osx-x86_64"
    )
    shadow(
        group = "io.netty",
        name = "netty-transport-native-kqueue",
        version = prop("dep-netty"),
        classifier = "osx-x86_64"
    )

    implementation(
        group = "io.netty",
        name = "netty-handler",
        version = prop("dep-netty")
    )
    shadow(
        group = "io.netty",
        name = "netty-handler",
        version = prop("dep-netty")
    )

    implementation(
        group = "io.netty",
        name = "netty-handler-proxy",
        version = prop("dep-netty")
    )
    shadow(
        group = "io.netty",
        name = "netty-handler-proxy",
        version = prop("dep-netty")
    )

}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_21.toString()
    targetCompatibility = JavaVersion.VERSION_21.toString()
    options.encoding = StandardCharsets.UTF_8.toString()
}

tasks.withType<AbstractArchiveTask> {
    isReproducibleFileOrder = true
    isPreserveFileTimestamps = false
}

tasks.withType<ShadowJar> {
    configurations = listOf(project.configurations.shadow.get())
    isZip64 = true
}

configurations.shadow { isTransitive = false }
