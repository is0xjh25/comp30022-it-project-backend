package tech.crm.crmserver.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrossOriginConfig implements WebMvcConfigurer{

    static final String ORIGINS[] = new String[] { "GET", "POST", "PUT", "DELETE" };
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080/")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods(ORIGINS).maxAge(3600);
    }

}
