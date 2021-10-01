package tech.crm.crmserver.config;


import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

/**
 * <p>
 *  The data format config
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-01
 */
@Configuration
public class DataFormatConfig {

    /**
     * format for LocalDateTime class
     */
    @Value("${spring.jackson.date-format}")
    private String pattern;


    /**
     * modified from https://www.baeldung.com/spring-boot-formatting-json-dates
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(pattern);
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern)));
            builder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern)));
        };
    }
}
