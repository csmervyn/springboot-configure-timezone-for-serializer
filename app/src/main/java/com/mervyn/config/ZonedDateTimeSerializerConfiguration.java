package com.mervyn.config;

import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;import java.util.TimeZone;

/**
 * @author cs-mervyn
 * @version 1.0
 * @date 2024/11/4 09:08
 */
@Configuration
public class ZonedDateTimeSerializerConfiguration {
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer configureTimeZoneJackson2ObjectMapperBuilderCustomizer() {
//        DateTimeFormatter dateTimeFormatterWithTimeZone = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssxxx")
//                .withZone(ZoneId.of("Asia/Shanghai"));
//        return builder -> {
//            builder.timeZone(TimeZone.getTimeZone("Asia/Shanghai"));
//           //builder.serializers(new ZonedDateTimeSerializer(dateTimeFormatter1));
//        };
//    }

}
