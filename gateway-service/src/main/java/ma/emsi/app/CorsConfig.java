package ma.emsi.app;

import org.springframework.context.annotation.*;
    import org.springframework.web.cors.*;
    import org.springframework.web.cors.reactive.*;
    import java.util.List;
    @Configuration
    public class CorsConfig {
      @Bean CorsWebFilter corsWebFilter(){
        CorsConfiguration config=new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("Content-Type"));
        var source=new org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);return new CorsWebFilter(source);
      }
    }
