//package com.example.qairlines.Utils;
//
//import com.google.common.base.Predicate;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.servers.Server;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.RequestHandler;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
//import java.util.List;
//
//
//@Configuration
//public class SpringFoxConfig {
//    @Bean
//    public OpenAPI openAPI(){
//        return new OpenAPI().info(new Info().title("Airlines documentation API").version("1.0.0"))
//                .servers(List.of(new Server().url("http://localhost:8080/")));
//    }
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis((Predicate<RequestHandler>) RequestHandlerSelectors.basePackage("com.example.qairlines"))  // Thay thế bằng package của bạn
//                .paths((Predicate<String>) PathSelectors.any())
//                .build();
//    }
//}
