package tech.crm.crmserver.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.crm.crmserver.common.constants.SecurityConstants;

import java.util.List;
import java.util.TimeZone;

/**
 * WebMvc configuration
 * @author Lingxiao
 * @since 2021-09-18
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

    /**
     * Configuration for CORS
     * define the CORS configuration
     */
    static final String ORIGINS[] = new String[] { "GET", "POST", "PUT", "DELETE" };
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080/","https://comp30022-yyds.herokuapp.com","http://localhost:3000/")
                .allowCredentials(true)
                .allowedHeaders("*")
                .exposedHeaders(SecurityConstants.TOKEN_HEADER)
                .allowedMethods(ORIGINS)
                .maxAge(3600);
    }

}
