package tech.crm.crmserver.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.crm.crmserver.common.constants.SecurityConstants;

@Configuration
public class CrossOriginConfig implements WebMvcConfigurer{

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
