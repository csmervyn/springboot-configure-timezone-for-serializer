# springboot-java-project-template

## Overview

Java project template build by gradle. Default integration some useful plugin.

## Build status

![Gradle Build](https://github.com/csmervyn/springboot-java-project-template/actions/workflows/gradle.yml/badge.svg)

## Prerequisites

- JDK 17
- Gradle

## Build in local

```shell
./gradlew clean build
```

## Framework we use

- Language: Java
- Test framework: Junit 5
- Build Tool: Gradle
- Mock framework: Mockito
- Assert framework: Hamcrest

## Plugins we integrate

- [checkstyle](./documents/plugins-we-integrate.md#checkstyle)
- [com.github.spotbugs](./documents/plugins-we-integrate.md#spotbugs)
- [org.owasp.dependencycheck](./documents/plugins-we-integrate.md#dependencycheck)
- [pmd](./documents/plugins-we-integrate.md#pmd)
- [gitleaks](./documents/plugins-we-integrate.md#gitleaks)
- [jacoco](./documents/plugins-we-integrate.md#Jacoco)

## Utils we use
- [MapStruct](./documents/utils-we-use.md#MapStruct)
- lombok

