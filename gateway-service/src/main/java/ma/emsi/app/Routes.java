package ma.emsi.app;

import org.springframework.context.annotation.*;
    import org.springframework.cloud.gateway.route.*;
    import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
    import org.springframework.core.env.Environment;
    @Configuration
    public class Routes {
      @Bean RouteLocator apiRoutes(RouteLocatorBuilder b,Environment env){
        boolean fixed=java.util.Arrays.asList(env.getActiveProfiles()).contains("static");
        return b.routes()
          .route("customers",r->r.path("/api/customers/**").filters(f->f.stripPrefix(1).dedupeResponseHeader("Access-Control-Allow-Origin Access-Control-Allow-Credentials", "RETAIN_FIRST")).uri(fixed?"http://localhost:8081":"lb://customer-service"))
          .route("accounts",r->r.path("/api/accounts/**").filters(f->f.stripPrefix(1).dedupeResponseHeader("Access-Control-Allow-Origin Access-Control-Allow-Credentials", "RETAIN_FIRST")).uri(fixed?"http://localhost:8082":"lb://ebank-service"))
          .route("chat",r->r.path("/api/chat/**").filters(f->f.stripPrefix(1).dedupeResponseHeader("Access-Control-Allow-Origin Access-Control-Allow-Credentials", "RETAIN_FIRST")).uri(fixed?"http://localhost:8084":"lb://chatbot-service"))
          .build();
      }
    }
