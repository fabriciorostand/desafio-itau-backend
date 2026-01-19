package com.itau.desafio.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String ATRIBUTO_TEMPO_INICIO = "tempoInicio";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long tempoInicio = System.currentTimeMillis();
        request.setAttribute(ATRIBUTO_TEMPO_INICIO, tempoInicio);

        log.info("{} {} - Recebida requisição", request.getMethod(), request.getRequestURI());

        log.debug("Parâmetros da URL da requisição: {}", request.getQueryString());
        log.debug("IP do cliente: {}", obterIpCliente(request));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long tempoInicio = (Long) request.getAttribute(ATRIBUTO_TEMPO_INICIO);

        long tempoDecorrido = tempoInicio != null
                ? System.currentTimeMillis() - tempoInicio
                : 0;

        int statusCode = response.getStatus();
        String metodo = request.getMethod();
        String uri = request.getRequestURI();

        if (statusCode >= 200 && statusCode < 300) {
            log.info("{} {} - Finalizado com sucesso [Status: {}] [Tempo: {}ms]", metodo, uri, statusCode, tempoDecorrido);
        } else if (statusCode >= 400 && statusCode < 500) {
            log.warn("{} {} - Erro do cliente [Status: {}] [Tempo: {}ms]", metodo, uri, statusCode, tempoDecorrido);
        } else if (statusCode >= 500) {
            log.error("{} {} - Erro do servidor [Status: {}] [Tempo: {}ms]", metodo, uri, statusCode, tempoDecorrido);
        }

        if (ex != null) {
            log.error("{} {} - Falha durante processamento fora do ControllerAdvice [Status: {}] [Tempo: {}ms]",
                    metodo, uri, statusCode, tempoDecorrido);
        }
    }

    private String obterIpCliente(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

}
