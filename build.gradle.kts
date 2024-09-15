import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.net.URI
import java.nio.charset.StandardCharsets
import java.util.*

plugins {
  id("idea")

  id("java")
  id("java-library")

  id("maven-publish")
  id("com.gradleup.shadow") version "8.3.0"
}

group = "net.cakemc.mc"
version = "0.0.0-develop"


val repoProperties = Properties()
val repoFile = file("/credentials.properties")
if (repoFile.exists())
  repoProperties.load(repoFile.inputStream())
val repoUsername: String = (repoProperties["username"] ?: System.getenv("REPOSITORY_USERNAME")).toString()
val repoPassword: String = (repoProperties["password"] ?: System.getenv("REPOSITORY_PASSWORD")).toString()

repositories {
  mavenCentral()

  maven {
    name = "cakemc-nexus"
    url = URI.create("http://cakemc.net:8081/repository/maven-releases")
    credentials {
      username = repoUsername
      password = repoPassword
    }
    isAllowInsecureProtocol = true
  }
}

@Suppress("unchecked_cast")
fun <V> prop(value: String): V {
  return properties.getValue(value) as V
}

dependencies {
  implementation(
    group = "net.cakemc.mc.server",
    name = "module-lib",
    version = "0.0.0-develop",
    classifier = "all"
  )
  shadow(
    group = "net.cakemc.mc.server",
    name = "module-lib",
    version = "0.0.0-develop",
    classifier = "all"
  )

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

  implementation(
    group = "net.cakemc.util",
    name = "screen-system",
    version = "0.0.0-develop",
    classifier = "all"
  )
  shadow(
    group = "net.cakemc.util",
    name = "screen-system",
    version = "0.0.0-develop",
    classifier = "all"
  )
}

val jdkVersion = JavaVersion.VERSION_21
val jdkVersionString = jdkVersion.toString()

java {
  toolchain.languageVersion = JavaLanguageVersion.of(jdkVersionString)
  withSourcesJar()
}

tasks.withType<JavaCompile> {
  sourceCompatibility = jdkVersionString
  targetCompatibility = jdkVersionString
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


//publishing {
//  publications {
//    create<MavenPublication>("mavenCakeNexus") {
//      from(components["java"])
//      artifact(tasks.shadowJar)
//
//      groupId = group.toString()
//      artifactId = "proxy"
//      version = version
//
//      pom {
//        name.set("CakeMc minecraft proxy")
//        description.set("The proxy of our server.")
//        url.set("https://github.com/CakeMC-Network/minecraft-proxy")
//        licenses {
//          license {
//            name.set("Apache-2.0")
//            url.set("https://www.apache.org/licenses/LICENSE-2.0")
//          }
//        }
//        developers {
//          developer {
//            id.set("CakeMcNET")
//            name.set("CakeMc")
//          }
//        }
//        scm {
//          connection = "scm:git:git://github.com/CakeMC-Network/minecraft-proxy.git"
//          developerConnection = "scm:git:ssh://github.com/CakeMC-Network/minecraft-proxy.git"
//          url = "github.com/CakeMC-Network/minecraft-proxy"
//        }
//      }
//    }
//  }
//  repositories {
//    maven {
//      name = "cakemc-nexus"
//      url = uri("http://cakemc.net:8081/")
//      credentials {
//        username = repoUsername
//        password = repoPassword
//      }
//      isAllowInsecureProtocol = true
//    }
//  }
//}
