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

        String metodo = request.getMethod();
        String uri = request.getRequestURI();
        String ip = obterIpCliente(request);

        log.info("{} {} | IP: {} - Recebida requisição", metodo, uri, ip);

        log.debug("Parâmetros da URL da requisição: {}", request.getQueryString());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long tempoInicio = (Long) request.getAttribute(ATRIBUTO_TEMPO_INICIO);

        long duracao = tempoInicio != null ? System.currentTimeMillis() - tempoInicio : 0;

        int status = response.getStatus();
        String metodo = request.getMethod();
        String uri = request.getRequestURI();

        if (ex != null) {
            log.error("{} {} - Falha durante processamento fora do ControllerAdvice [{}ms] - Status: {}",
                    metodo, uri, duracao, status);
        } else if (status >= 200 && status < 300) {
            log.info("{} {} - Finalizado com sucesso [{}ms] - Status: {}", metodo, uri, duracao, status);
        } else if (status >= 400 && status < 500) {
            log.warn("{} {} - Erro do cliente [{}ms] - Status: {}", metodo, uri, duracao, status);
        } else if (status >= 500) {
            log.error("{} {} - Erro do servidor [{}ms] - Status: {}", metodo, uri, duracao, status);
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
