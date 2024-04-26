//package com.example.gatewayservice.config;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RouteConfig {
//
////    @Bean
////    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
////        return builder.routes()
////                .route("service1", r -> r.path("/service1/**")
////                        .filters(f -> f.stripPrefix(1))
////                        .uri("lb://SERVICE01"))
////                .route("service2", r -> r.path("/service2/**")
////                        .filters(f -> f.stripPrefix(1))
////                        .uri("lb://SERVICE02"))
////                .build();
////    }
//
//    @Bean
//    public RouteLocator customRouteLocator2(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("service1", r -> r.path("/service1v2/**")
//                        .filters(f -> f.stripPrefix(1))
//                        .uri("lb://SERVICE01"))
//                .route("service2", r -> r.path("/service2v2/**")
//                        .filters(f -> f.stripPrefix(1))
//                        .uri("lb://SERVICE02"))
//                .build();
//    }
//}