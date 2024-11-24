# Spring Boot 中如何设置 serializer 的 TimeZone
## 1 背景

在一个基于 Spring Boot 的服务中，对外提供一个 Restful 接口 A。该接口的返回值类型中包含一个 ZonedDateTime 类型的字段 createdDateTime。Prod 代码侧期望返回时区为 Australia/Sydney（UTC+10/UTC+10）的时间，并带有时区信息，例如，2024-11-03T08:23:24+11:00。并且测试侧代码也是按照期望进行了测试，测试在 Pipeline 和澳洲 Dev 的本地电脑中可以通过。测试示例如代码1所示。但是该代码确不能在中国 Dev 的本地电脑上工作。

在同样的测试数据情况下，A 接口的响应中 createdDateTime 字段的值，因代码运行所在时区的不同而不同。具体来说，假如 ZonedDateTime 类型的 createdDateTime 字段在 Java 值为 2024-11-03T08:23:24+11:00[Australia/Sydney]。如果在澳洲运行测试代码，A 接口的响应为 `{"createdDateTime": 2024-11-03T08:23:24+11:00}`。但是如果在中国运行测试代码，A 接口的响应为 `{"createdDateTime": 2024-11-03T05:23:24+08:00}`。通过对比澳洲和中国的 A 接口的响应可以发现 createdDateTime 字段值，实际代表同一个时间但仅是时区不一样。一个时间是 UTC+11（Australia/Sydney），另一个是UTC+8（Asia/Shanghai）。但是从生产代码和测试代码的角度上来讲，其期望 A 接口的响应中的 createdDateTime 一直返回面向 UTC+10/UTC+11 的时间。测试代码示例，如代码清单 1 所示。
代码清单 1: 测试 A 接口的代码示例 UserControllerTest.java
```java
    void theTimeZoneOfCreatedDateTimeShouldBeAustraliaSydney() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].createdDateTime", is(notNullValue())))
                .andExpect(jsonPath("$[0].createdDateTime", anyOf(containsString("+11:00"), containsString("+10:00"))));
    }
```

## 2 问题的分析
首先对问题的初步定位是，这是因为对象序列化方面的问题。首先我们了解到 Spring Boot 使用不同的 HttpMessageConverter 来处理不同的 MediaTypes 的响应。默认 Spring Boot 通过 Jackson 库，并使用 MappingJackson2HttpMessageConverter 处理 MediaType 为 `application/json` 或 `application/*+json` 的响应。MappingJackson2HttpMessageConverter 针对不同的数据类型，会利用 jackson-databind 库的或自定义的 Serializer 和 Deserializer，对接口的返回结果或请求参数进行序列化和反序列化。在这里我们遇到是关于序列化的问题，因此在这里目前我们仅仅关注序列化。例如 ZonedDateTime 类型的数据，MappingJackson2HttpMessageConverter 便是通过 jackson-databind 库中的 ObjectMapper 和该库中内建的 ZonedDateTimeSerializer，完成对 ZonedDateTime 类型的对象的序列化。在继续分析之前，我们需要先了解 Spring Boot 中如何构建 MappingJackson2HttpMessageConverter 和 ObjectMapper 对象的。

### 2.1 Spring Boot 中如何自动装配 MappingJackson2HttpMessageConverter？
如代码清单2所示，在 JacksonHttpMessageConvertersConfiguration 类 Spring Boot 使用 ObjectMapper 来构建 MappingJackson2HttpMessageConverter 对象。那 Spring Boot 中如何装配 ObjectMapper 对象呢？

代码清单 2: Spring Boot 中自动装配 MappingJackson2HttpMessageConverter 的代码片段 JacksonHttpMessageConvertersConfiguration.java
```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ObjectMapper.class)
@ConditionalOnBean(ObjectMapper.class)
@ConditionalOnProperty(name = HttpMessageConvertersAutoConfiguration.PREFERRED_MAPPER_PROPERTY,
        havingValue = "jackson", matchIfMissing = true)
static class MappingJackson2HttpMessageConverterConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = MappingJackson2HttpMessageConverter.class,
            ignoredType = {
                    "org.springframework.hateoas.server.mvc.TypeConstrainedMappingJackson2HttpMessageConverter",
                    "org.springframework.data.rest.webmvc.alps.AlpsJsonHttpMessageConverter" })
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

}
```
### 2.2 Spring Boot 中如何自动装配 ObjectMapper？
如代码清单3所示，在 JacksonAutoConfiguration 中，Spring Boot 中使用 Jackson2ObjectMapperBuilder 来自动装配 ObjectMapper 对象。Jackson2ObjectMapperBuilder 的 build 的方法中会注册一些常用的 Module 例如，Jdk8Module、ParameterNamesModule 和 JavaTimeModule 以及 KotlinModule 等。那 Spring Boot 中如何装配 Jackson2ObjectMapperBuilder 对象呢？

代码清单 3: Spring Boot 中自动装配 ObjectMapper 的代码片段 JacksonAutoConfiguration.java
```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
static class JacksonObjectMapperConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean
    ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.createXmlMapper(false).build();
    }

}
```
### 2.3 Spring Boot 中如何自动装配 Jackson2ObjectMapperBuilder？
如代码清单4所示，在 JacksonAutoConfiguration 中，Spring Boot 中使用 List<Jackson2ObjectMapperBuilderCustomizer> 来构建 Jackson2ObjectMapperBuilder 对象。并使用 List<Jackson2ObjectMapperBuilderCustomizer> 对 Jackson2ObjectMapperBuilder 对象进行自定义。Jackson2ObjectMapperBuilder 会调用所有 List<Jackson2ObjectMapperBuilderCustomizer> 的 customize 方法，从而对 Jackson2ObjectMapperBuilder 对象进行配置。那 Spring Boot 中如何装配 Jackson2ObjectMapperBuilderCustomizer 对象呢？
代码清单 4: Spring Boot 中自动装配 Jackson2ObjectMapperBuilder 的代码片段 JacksonAutoConfiguration.java
```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
static class JacksonObjectMapperBuilderConfiguration {

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder(ApplicationContext applicationContext,
            List<Jackson2ObjectMapperBuilderCustomizer> customizers) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.applicationContext(applicationContext);
        customize(builder, customizers);
        return builder;
    }

    private void customize(Jackson2ObjectMapperBuilder builder,
            List<Jackson2ObjectMapperBuilderCustomizer> customizers) {
        for (Jackson2ObjectMapperBuilderCustomizer customizer : customizers) {
            customizer.customize(builder);
        }
    }

}
```
### 2.4 Spring Boot 中如何自动装配 Jackson2ObjectMapperBuilderCustomizer？
如代码清单4所示，在 JacksonAutoConfiguration 中，Spring Boot 实现了一个 Jackson2ObjectMapperBuilderCustomizer 接口的 StandardJackson2ObjectMapperBuilderCustomizer，并向容器中该类型的对象。在 StandardJackson2ObjectMapperBuilderCustomizer 的 customize 方法会从配置文件中读取 jackson 相关的属性来配置 Jackson2ObjectMapperBuilder。当然我们也可以自定义自己的 Jackson2ObjectMapperBuilderCustomizer 接口的实现类，从而用来配置 Jackson2ObjectMapperBuilder。 

代码清单 5: Spring Boot 中自动装配 Jackson2ObjectMapperBuilderCustomizer 的代码片段 JacksonAutoConfiguration.java
```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
@EnableConfigurationProperties(JacksonProperties.class)
static class Jackson2ObjectMapperBuilderCustomizerConfiguration {

    @Bean
    StandardJackson2ObjectMapperBuilderCustomizer standardJacksonObjectMapperBuilderCustomizer(
            JacksonProperties jacksonProperties, ObjectProvider<Module> modules) {
        return new StandardJackson2ObjectMapperBuilderCustomizer(jacksonProperties, modules.stream().toList());
    }

    static final class StandardJackson2ObjectMapperBuilderCustomizer
            implements Jackson2ObjectMapperBuilderCustomizer, Ordered {

        private final JacksonProperties jacksonProperties;

        private final Collection<Module> modules;

        StandardJackson2ObjectMapperBuilderCustomizer(JacksonProperties jacksonProperties,
                                                      Collection<Module> modules) {
            this.jacksonProperties = jacksonProperties;
            this.modules = modules;
        }

        @Override
        public int getOrder() {
            return 0;
        }

        @Override
        public void customize(Jackson2ObjectMapperBuilder builder) {
            if (this.jacksonProperties.getDefaultPropertyInclusion() != null) {
                builder.serializationInclusion(this.jacksonProperties.getDefaultPropertyInclusion());
            }
            if (this.jacksonProperties.getTimeZone() != null) {
                builder.timeZone(this.jacksonProperties.getTimeZone());
            }
            configureFeatures(builder, FEATURE_DEFAULTS);
            configureVisibility(builder, this.jacksonProperties.getVisibility());
            configureFeatures(builder, this.jacksonProperties.getDeserialization());
            configureFeatures(builder, this.jacksonProperties.getSerialization());
            configureFeatures(builder, this.jacksonProperties.getMapper());
            configureFeatures(builder, this.jacksonProperties.getParser());
            configureFeatures(builder, this.jacksonProperties.getGenerator());
            configureFeatures(builder, this.jacksonProperties.getDatatype().getEnum());
            configureFeatures(builder, this.jacksonProperties.getDatatype().getJsonNode());
            configureDateFormat(builder);
            configurePropertyNamingStrategy(builder);
            configureModules(builder);
            configureLocale(builder);
            configureDefaultLeniency(builder);
            configureConstructorDetector(builder);
        }
    }
    // .....
}
```

### 2.5 ZonedDateTimeSerializer 如何对 ZonedDateTime 对象进行序列化。
通过 2.1 至 2.4 节的简述，我们已经了解了如 Spring Boot 中如何构建 MappingJackson2HttpMessageConverter 和 ObjectMapper 对象。紧接着，我们了解 ZonedDateTimeSerializer 如何对 ZonedDateTime 对象进行序列化。ZonedDateTimeSerializer 会使用其父类 InstantSerializerBase 的 formatValue 方法对 ZonedDateTime 对象进行序列化和格式化，如代码清单6所示。

代码清单 6: InstantSerializerBase 对 ZonedDateTime 对象进行序列化和格式化的方法 InstantSerializerBase.java
```java
protected String formatValue(T value, SerializerProvider provider) {
    DateTimeFormatter formatter = this._formatter != null ? this._formatter : this.defaultFormat;
    if (formatter != null) {
        if (formatter.getZone() == null && provider.getConfig().hasExplicitTimeZone() && provider.isEnabled(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)) {
            formatter = formatter.withZone(provider.getTimeZone().toZoneId());
        }

        return formatter.format(value);
    } else {
        return value.toString();
    }
}
```

从代码清单6可以看出，如果想要对 ZonedDateTime 对象进行格式化和序列化，可以通过以下两种思路：
- 思路一：设置 InstantSerializerBase 的 defaultFormat 属性或者其父类（JSR310FormattedSerializerBase）的 _formatter 属性的 TimeZone。
- 思路二：如果 InstantSerializerBase 的 defaultFormat 属性或者其父类的 _formatter 属性没有设置 TimeZone，则需要设置 SerializerProvider 的 TimeZone。

## 3 问题的解决方案
### 3.1 基于思路一的解决方案
#### 3.1.1 方法一：通过自定义的 Jackson2ObjectMapperBuilderCustomizer 设置 InstantSerializerBase 的 defaultFormat 属性的 TimeZone。
默认情况下，JSR310FormattedSerializerBase 的 _formatter 属性的优先级高于 InstantSerializerBase 的 defaultFormat。JavaTimeModule 在注册 ZonedDateTimeSerializer 时，使用其无参构造方法构造的 ZonedDateTimeSerializer 对象的祖父 JSR310FormattedSerializerBase 的 _formatter 属性为 null，其父类 InstantSerializerBase 的 defaultFormat 属性，默认为 DateTimeFormatter.ISO_OFFSET_DATE_TIME。DateTimeFormatter.ISO_OFFSET_DATE_TIME 属于并没有指定 TimeZone 的 DateTimeFormatter 对象。但是 ZonedDateTimeSerializer 类提供了公开的构造方法 `public ZonedDateTimeSerializer(DateTimeFormatter formatter)`，因此我们可以使用 ZonedDateTimeSerializer 的构造方法，并提供已指定时区的 DateTimeFormatter。并且基于 2.1 至 2.4 的先验知识，因此我们可以向 ApplicationContext 中注入一个 Jackson2ObjectMapperBuilderCustomizer，来向 Jackson2ObjectMapperBuilder 中添加指定了 TimeZone 的 DateTimeFormatter 对象的 ZonedDateTimeSerializer。并且通过 Jackson2ObjectMapperBuilder 设置的 ZonedDateTimeSerializer 会覆盖默认的 JavaTimeModule 中的 ZonedDateTimeSerializer 对象，示例代码如代码清单7所示。

代码清单 7: 通过自定义的 Jackson2ObjectMapperBuilderCustomizer 设置 InstantSerializerBase 的 defaultFormat 属性的 TimeZone  ZonedDateTimeSerializerConfiguration.java

```java
@Bean
public Jackson2ObjectMapperBuilderCustomizer configureTimeZoneJackson2ObjectMapperBuilderCustomizer() {
    DateTimeFormatter dateTimeFormatterWithTimeZone = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssxxx")
            .withZone(ZoneId.of("Asia/Shanghai"));
    return builder -> {
       builder.serializers(new ZonedDateTimeSerializer(dateTimeFormatterWithTimeZone));
    };
}
```
#### 3.1.2 方法二：通过携带 timezone 属性的 @JsonFormat 从而设置 InstantSerializerBase 的父类（JSR310FormattedSerializerBase）的 _formatter 属性的 TimeZone。
如代码清单8所示，我们可以通过表注携带 timezone 属性的 @JsonFormat 到指定的字段上，来达到设置 InstantSerializerBase 的父类（JSR310FormattedSerializerBase）的 _formatter 属性的 TimeZone 的目的。为什么表注 @JsonFormat，可以达到设置 InstantSerializerBase 的父类（JSR310FormattedSerializerBase）的 _formatter 属性的 TimeZone 的目的。

代码清单 8: 通过携带 timezone 属性的 @JsonFormat 从而设置 InstantSerializerBase 的父类（JSR310FormattedSerializerBase）的 _formatter 属性的 TimeZone  UserDto.java
```java
public class UserDto {
    private String name;
    private Integer age;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss:ssXXX", timezone = "GMT+8")
    private ZonedDateTime createdDateTime;
}
```

通过代码清单9，我们可以看到在 JSR310FormattedSerializerBase 的 createContextual 方法中，会获取 @JsonFormat 的信息，并根据其中的信息调用其抽象方法 withFormat 来设置 InstantSerializerBase 的父类（JSR310FormattedSerializerBase）的 _formatter 属性。通过代码清单10，我们可以看到 JSR310FormattedSerializerBase 的 useDateTimeFormatter 方法会根据 @JsonFormat 的信息，构造一个 DateTimeFormatter 对象。
代码清单 9: 通过 @JsonFormat 设置 ZonedDateTimeSerializer 的 _formatter 属性的代码片段   JSR310FormattedSerializerBase.java
```java
public JsonSerializer<?> createContextual(SerializerProvider prov,
        BeanProperty property) throws JsonMappingException
{
    JsonFormat.Value format = findFormatOverrides(prov, property, handledType());
    if (format != null) {
        Boolean useTimestamp = null;

       // Simple case first: serialize as numeric timestamp?
        JsonFormat.Shape shape = format.getShape();
        if (shape == JsonFormat.Shape.ARRAY || shape.isNumeric() ) {
            useTimestamp = Boolean.TRUE;
        } else {
            useTimestamp = (shape == JsonFormat.Shape.STRING) ? Boolean.FALSE : null;
        }
        DateTimeFormatter dtf = _formatter;

        // If not, do we have a pattern?
        if (format.hasPattern()) {
            dtf = _useDateTimeFormatter(prov, format);
        }
        JSR310FormattedSerializerBase<?> ser = this;
        if ((shape != _shape) || (useTimestamp != _useTimestamp) || (dtf != _formatter)) {
            ser = ser.withFormat(useTimestamp, dtf, shape);
        }
        Boolean writeZoneId = format.getFeature(JsonFormat.Feature.WRITE_DATES_WITH_ZONE_ID);
        Boolean writeNanoseconds = format.getFeature(JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        if ((writeZoneId != null) || (writeNanoseconds != null)) {
            ser = ser.withFeatures(writeZoneId, writeNanoseconds);
        }
        return ser;
    }
    return this;
}
```

代码清单 10: 解析 @JsonFormat 构造 DateTimeFormatter 的 _useDateTimeFormatter  JSR310FormattedSerializerBase.java
```java
protected DateTimeFormatter _useDateTimeFormatter(SerializerProvider prov, JsonFormat.Value format) {
    DateTimeFormatter dtf;
    final String pattern = format.getPattern();
    final Locale locale = format.hasLocale() ? format.getLocale() : prov.getLocale();
    if (locale == null) {
        dtf = DateTimeFormatter.ofPattern(pattern);
    } else {
        dtf = DateTimeFormatter.ofPattern(pattern, locale);
    }
    //Issue #69: For instant serializers/deserializers we need to configure the formatter with
    //a time zone picked up from JsonFormat annotation, otherwise serialization might not work
    if (format.hasTimeZone()) {
        dtf = dtf.withZone(format.getTimeZone().toZoneId());
    }
    return dtf;
}
```

### 3.2 基于思路二的解决方案
通过调试和代码分析，我们可知 SerializerProvider 的 TimeZone 来自于 ObjectMapper 的 TimeZone，因此仅仅需要设置 ObjectMapper 的 TimeZone 即可。有多种方式可以设置 ObjectMapper 的 TimeZone。
#### 3.2.1 方法一：通过 Spring Boot Common Application Properties 中的 spring.jackson.time-zone 配置 ObjectMapper 的 TimeZone
我们可以在 Spring Boot 的 [Common Application Properties](https://docs.spring.io/spring-boot/appendix/application-properties/index.html) 中找到配置 ObjectMapper 的 TimeZone 的属性：spring.jackson.time-zone，其参数的格式为，"America/Los_Angeles" or "GMT+10", 如代码清单11 所示。
代码清单 11: 设置 ObjectMapper 的 TimeZone 的属性   application.yaml
```yaml
spring:
  jackson:
    time-zone: Asia/Shanghai
```
为啥配置了该属性，就能配置 ObjectMapper 的 TimeZone？因为 Spring Boot 配置了一个 StandardJackson2ObjectMapperBuilderCustomizer，该 StandardJackson2ObjectMapperBuilderCustomizer 会读取 spring.jackson.time-zone 的值，并设置 Jackson2ObjectMapperBuilder 的 TimeZone，如代码清单 5所示。Jackson2ObjectMapperBuilder 在构建 ObjectMapper 时，会根据其中的 TimeZone，设置 ObjectMapper 的 TimeZone。
#### 3.2.2 方法二：通过自定义的 Jackson2ObjectMapperBuilderCustomizer 配置 ObjectMapper 的 TimeZone
通过代码清单4，我们了解到，JacksonObjectMapperBuilderConfiguration 在构建 Jackson2ObjectMapperBuilder 时，会从 ApplicationContext 中获取所有 Jackson2ObjectMapperBuilderCustomizer 类型的对象，并调用他们 customize 对 Jackson2ObjectMapperBuilder 进行自定义。因此我们可以向 ApplicationContext 中注入一个 Jackson2ObjectMapperBuilderCustomizer，来设置 Jackson2ObjectMapperBuilder 的 TimeZone，进而达到设置 ObjectMapper 的 TimeZone 的目的，其示例代码如代码清单12所示。

代码清单 12: 通过自定义的 Jackson2ObjectMapperBuilderCustomizer 设置 ObjectMapper 的 TimeZone  ZonedDateTimeSerializerConfiguration.java
```java
@Bean
public Jackson2ObjectMapperBuilderCustomizer configureTimeZoneJackson2ObjectMapperBuilderCustomizer() {
    return builder -> {
        builder.timeZone(TimeZone.getTimeZone("Asia/Shanghai"));
    };
}
```


## 4 解决背景中引入的问题
回到背景中描述的问题本身，我们也发现了导致背景中描述问题的根本原因。因为其生产代码通过自定义的 Jackson2ObjectMapperBuilderCustomizer 设置 ObjectMapper 的 TimeZone 为 `TimeZone.getDefault()`，如代码清单13所示。`TimeZone.getDefault()` 会根据获取 JVM 默认的 TimeZone。例如在应用在 Sydney 该值为 `Australia/Sydney`；应用在中国，该值为 `Asia/Shanghai`。这就解释了背景中描述的现象。最后我们根据业务实际的要求，将该 ObjectMapper 的 TimeZone 为固定设置为 `Australia/Sydney`，最终解决了该问题。
代码清单 13: 通过自定义的 Jackson2ObjectMapperBuilderCustomizer 设置 ObjectMapper 的 TimeZone 为 TimeZone.getDefault()  ZonedDateTimeSerializerConfiguration.java
```java
@Bean
public Jackson2ObjectMapperBuilderCustomizer configureTimeZoneJackson2ObjectMapperBuilderCustomizer() {
    return builder -> {
        builder.timeZone(TimeZone.getDefault());
    };
}
```
## 5 Reference
- [Spring Boot: Customize the Jackson ObjectMapper](https://www.baeldung.com/spring-boot-customize-jackson-objectmapper)
- [Jackson 配置与扩展](https://www.herodotus.vip/guide/design/jackson.html)
- [@JsonFormat 实现原理](https://www.jianshu.com/p/1031c09da1db)
