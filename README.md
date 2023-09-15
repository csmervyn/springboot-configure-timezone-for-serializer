# gradle-java-project-template

## Overview

Java project template build by gradle. Default integration some useful plugin.

## Build status

![Gradle Build](https://github.com/csmervyn/gradle-java-project-template/actions/workflows/gradle.yml/badge.svg)

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

- checkstyle
- spotbugs
- dependencycheck
- pmd
- gitleaks
- jacoco

## Utils we use
- mapStruct
- lombok

### checkstyle

本地运行 checkstyle 检查

```shell
./gradlew clean check
```

上面的命令在运行时，会在命令行给出违法 checkstyle 规则的提示和生成 checkstyle 报告。
生产代码 checkstyle 报告所在位置:

```shell
{项目 root directory}/app/build/reports/checkstyle/main.html
```

test 代码 checkstyle 报告所在位置:

```shell
{项目 root directory}/app/build/reports/checkstyle/test.html
```

#### reference

- [The Checkstyle Plugin](https://docs.gradle.org/current/userguide/checkstyle_plugin.html#sec:checkstyle_configuration)
- [release list of checkstyle](https://checkstyle.sourceforge.io/releasenotes.html)

### spotbugs

本地运行 spotbugs 检查

```shell
./gradlew clean check
```

上面的命令在运行时，会检查潜在的bug 和生成 spotbugs 报告。
spotbugs 报告所在位置:

```shell
{项目 root directory}/app/build/reports/spotbugs.html
```

#### reference

- [spotbugs-gradle-plugin Github](https://github.com/spotbugs/spotbugs-gradle-plugin)
- [Gradle plugin portal](https://plugins.gradle.org/plugin/com.github.spotbugs)

### dependencycheck

本地运行 dependencycheck 的检查

```shell
./gradlew dependencyCheckAnalyze
```

上面的命令在运行时，在命令行检查 dependency 的 vulnerabilities和生成 dependency-check-report 报告。
dependency-check-report 报告所在位置:

```shell
{项目 root directory}/app/build/reports/dependency-check-report.html
```

#### reference

- [Gradle dependencycheck plugin](https://plugins.gradle.org/plugin/org.owasp.dependencycheck)
- [Dependencycheck documention](http://jeremylong.github.io/DependencyCheck/dependency-check-gradle/index.html)

### pmd

本地运行 pmd 的检查

```shell
./gradlew clean check
```

上面的命令在运行时，在命令行检查 pmd 和生成 pmd 报告。
生产代码 pmd 报告所在位置:

```shell
{项目 root directory}/app/build/reports/pmd/main.html
```

test 代码 pmd 报告所在位置:

```shell
{项目 root directory}/app/build/reports/pmd/test.html
```

#### reference

- [Guide pmd plugin](https://docs.gradle.org/current/userguide/pmd_plugin.html)

### gitleaks

首先需要安装 git hook 脚步：

```shell
pre-commit install
```

测试 gitleaks 是否生效：

```shell
pre-commit run --all-files
```

#### reference

- [pre-commit document](https://pre-commit.com/)
- [hooks of pre-commit](https://pre-commit.com/hooks.html)
- [github gitleaks](https://github.com/gitleaks/gitleaks)

### Jacoco

本地运行 jacoco 生成报告的命令：

```shell
./gradlew clean build
# or
./gradlew clean jacocoTestReport
```

jacoco 报告所在位置:

```shell
{项目 root directory}/app/build/jacocoHtml/index.html
```

本地运行 jacoco 检查报告的命令：

```shell
./gradlew clean build
# or
./gradlew clean jacocoTestCoverageVerification
```

#### reference

- [Gradle jacoco](https://docs.gradle.org/current/userguide/jacoco_plugin.html)

## MapStruct

#### reference
- [MapStruct](https://mapstruct.org/)
- [MapStruct installation](https://mapstruct.org/documentation/installation/)

