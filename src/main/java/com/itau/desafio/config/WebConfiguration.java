package com.itau.desafio.config;

import com.itau.desafio.logging.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor ;

    /* Configura um interceptador de logs para ser aplicado em todas as rotas da aplicação,
    exceto as da documentação Swagger, garantindo que em cada requisição seja registrado logs antes de prosseguir,
    para monitoramento e depuração sem poluir os endpoints de API. */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                );
    }

}