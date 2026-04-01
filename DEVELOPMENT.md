# Development Setup

This repository is currently configured for:

- Minecraft `1.20.1`
- Forge `47.2.0`
- Gradle wrapper `8.1.1`
- Java `17` (`java.toolchain.languageVersion = 17`)

## Prerequisites

Install:

1. JDK (required):
- Java `17`
2. Git
3. Internet access for Gradle dependency downloads

You do not need a global Gradle installation because this repo ships `./gradlew`.

## Quick Start

From the project root:

```bash
chmod +x gradlew
./gradlew --version
./gradlew build
```

Run the development client:

```bash
./gradlew runClient
```

Run the development server:

```bash
./gradlew runServer
```

## Java Setup (SDKMAN)

Example with SDKMAN:

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 17.0.10-tem
sdk use java 17.0.10-tem
```

Verify:

```bash
java -version
javac -version
./gradlew --version
```

## 1.20.1 Migration Checklist

Validate with:

```bash
GRADLE_USER_HOME=$PWD/.gradle-user ./gradlew clean build
GRADLE_USER_HOME=$PWD/.gradle-user ./gradlew runClient
```
