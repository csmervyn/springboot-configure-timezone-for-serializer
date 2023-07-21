# gradle-java-project-template
集成的组件有：
- checkstyle
- spotbugs
- dependencycheck

## checkstyle
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
### reference
[The Checkstyle Plugin](https://docs.gradle.org/current/userguide/checkstyle_plugin.html#sec:checkstyle_configuration)
[release list of checkstyle](https://checkstyle.sourceforge.io/releasenotes.html)

## spotbugs
本地运行 spotbugs 检查
```shell
./gradlew clean check
```
上面的命令在运行时，会检查潜在的bug 和生成 spotbugs 报告。
spotbugs 报告所在位置:
```shell
{项目 root directory}/app/build/reports/spotbugs.html
```
### reference
[spotbugs-gradle-plugin Github](https://github.com/spotbugs/spotbugs-gradle-plugin)
[Gradle plugin portal](https://plugins.gradle.org/plugin/com.github.spotbugs)

## dependencycheck
本地运行 dependencycheck 的检查
```shell
./gradlew dependencyCheckAnalyze
```
上面的命令在运行时，在命令行检查 dependency 的 vulnerabilities和生成 dependency-check-report 报告。
dependency-check-report 报告所在位置:
```shell
{项目 root directory}/app/build/reports/dependency-check-report.html
```

### reference
[Gradle dependencycheck plugin](https://plugins.gradle.org/plugin/org.owasp.dependencycheck)
[Dependencycheck documention](http://jeremylong.github.io/DependencyCheck/dependency-check-gradle/index.html)

