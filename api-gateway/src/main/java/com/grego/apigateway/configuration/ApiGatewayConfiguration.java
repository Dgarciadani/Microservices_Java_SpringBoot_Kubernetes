package com.grego.apigateway.configuration;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
               .route(p->p.path("/get")
                       // the filters are in the middle of the handling and process the request
                       .filters(f->f
                               .addRequestHeader("MyHeader","MyURI")
                               .addRequestParameter("MyParam", "MyValue")
                       )
                       .uri("http://httpbin.org:80"))
                //when we require the path we are redirected to the uri
                .route(p->p.path("/currency-exchange/**")
                        //to use the load balancing (lb:// with the name in eureka
                        .uri("lb://currency-exchange"))
                .route(p->p.path("/currency-conversion/**")
                        .uri("lb://currency-conversion-service"))
                .route(p->p.path("/currency-conversion-feign/**")
                        .uri("lb://currency-conversion-service"))
                .route(p->p.path("/currency-conversion-new/**")
                        //we add (?<segment>.*) to create a regex to transfer the rest of the path, and we call it using ${segment}
                        .filters(f->f.rewritePath("/currency-conversion-new/(?<segment>.*)","/currency-conversion-feign/${segment}"))
                        .uri("lb://currency-conversion-service"))
               .build();
    }
}
