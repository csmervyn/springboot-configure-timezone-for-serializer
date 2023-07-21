# gradle-java-project-template
集成的组件有：
- checkstyle

## checkstyle
本地运行 checkstyle 检查
```shell
./gradlew clean check
```
上面的命令在运行时，会在命令行给出违法 checkstyle 规则的提示和生产 checkstyle 报告。
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
