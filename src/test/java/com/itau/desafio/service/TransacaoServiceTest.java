package com.itau.desafio.service;

import com.itau.desafio.domain.transacao.TransacaoService;
import com.itau.desafio.dto.EstatisticaResponse;
import com.itau.desafio.dto.RegistrarTransacaoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class TransacaoServiceTest {

    private TransacaoService service;

    private Clock clock;

    @BeforeEach
    void setService() {
        clock = Clock.fixed(
                Instant.parse("2026-01-15T12:00:00Z"),
                ZoneOffset.UTC
        );

        service = new TransacaoService(clock);
    }

    @Test
    @DisplayName("Deve retornar estatísticas zeradas quando não houver transações")
    void deveRetornarEstatisticasZeradasQuandoNaoHouverTransacoes() {
        EstatisticaResponse response = service.buscarEstatisticas(60L);

        assertThat(response.count()).isZero();
        assertThat(response.sum()).isZero();
        assertThat(response.avg()).isZero();
        assertThat(response.min()).isZero();
        assertThat(response.max()).isZero();
    }

    @Test
    @DisplayName("Deve calcular estatísticas corretamente para transações dentro do intervalo")
    void deveCalcularEstatisticasCorretamente() {
        service.registrar(new RegistrarTransacaoRequest(200, OffsetDateTime.now(clock).minusSeconds(10)));
        service.registrar(new RegistrarTransacaoRequest(100, OffsetDateTime.now(clock).minusSeconds(20)));

        EstatisticaResponse response = service.buscarEstatisticas(60L);

        assertThat(response.count()).isEqualTo(2);
        assertThat(response.sum()).isEqualTo(300);
        assertThat(response.avg()).isEqualTo(150);
        assertThat(response.min()).isEqualTo(100);
        assertThat(response.max()).isEqualTo(200);
    }

    @Test
    @DisplayName("Deve processar transações com valor zero")
    void deveProcessarTransacoesComValorZero() {
        service.registrar(new RegistrarTransacaoRequest(0, OffsetDateTime.now(clock).minusSeconds(10)));
        service.registrar(new RegistrarTransacaoRequest(100, OffsetDateTime.now(clock).minusSeconds(20)));

        EstatisticaResponse response = service.buscarEstatisticas(60L);

        assertThat(response.count()).isEqualTo(2);
        assertThat(response.sum()).isEqualTo(100);
        assertThat(response.avg()).isEqualTo(50);
        assertThat(response.min()).isEqualTo(0);
        assertThat(response.max()).isEqualTo(100);
    }

    @Test
    @DisplayName("Deve calcular estatísticas corretamente para transações no limite exato do intervalo")
    void deveCalcularEstatisticasCorretamenteNoLimiteDoIntervalo() {
        service.registrar(new RegistrarTransacaoRequest(200, OffsetDateTime.now(clock).minusSeconds(60)));
        service.registrar(new RegistrarTransacaoRequest(100, OffsetDateTime.now(clock).minusSeconds(60)));

        EstatisticaResponse response = service.buscarEstatisticas(60L);

        assertThat(response.count()).isEqualTo(2);
        assertThat(response.sum()).isEqualTo(300);
        assertThat(response.avg()).isEqualTo(150);
        assertThat(response.min()).isEqualTo(100);
        assertThat(response.max()).isEqualTo(200);
    }

    @Test
    @DisplayName("Deve ignorar transações fora do intervalo de tempo")
    void deveIgnorarTransacoesForaDoIntervalo() {
        service.registrar(new RegistrarTransacaoRequest(200, OffsetDateTime.now(clock).minusSeconds(10)));
        service.registrar(new RegistrarTransacaoRequest(100, OffsetDateTime.now(clock).minusSeconds(20)));
        service.registrar(new RegistrarTransacaoRequest(50, OffsetDateTime.now(clock).minusSeconds(80)));

        EstatisticaResponse response = service.buscarEstatisticas(60L);

        assertThat(response.count()).isEqualTo(2);
        assertThat(response.sum()).isEqualTo(300);
        assertThat(response.avg()).isEqualTo(150);
        assertThat(response.min()).isEqualTo(100);
        assertThat(response.max()).isEqualTo(200);
    }

    @Test
    @DisplayName("Deve retornar estatísticas zeradas quando todas as transações estiverem fora do intervalo")
    void deveRetornarEstatisticasZeradasQuandoTodasEstiveremForaDoIntervalo() {
        service.registrar(new RegistrarTransacaoRequest(200, OffsetDateTime.now(clock).minusSeconds(80)));
        service.registrar(new RegistrarTransacaoRequest(100, OffsetDateTime.now(clock).minusSeconds(100)));

        EstatisticaResponse response = service.buscarEstatisticas(60L);

        assertThat(response.count()).isZero();
        assertThat(response.sum()).isZero();
        assertThat(response.avg()).isZero();
        assertThat(response.min()).isZero();
        assertThat(response.max()).isZero();
    }

    @Test
    @DisplayName("Deve limpar todas as transações")
    void deveLimparTodasTransacoes() {
        service.registrar(new RegistrarTransacaoRequest(200, OffsetDateTime.now(clock).minusSeconds(10)));
        service.registrar(new RegistrarTransacaoRequest(100, OffsetDateTime.now(clock).minusSeconds(20)));

        service.limpar();

        EstatisticaResponse response = service.buscarEstatisticas(60L);

        assertThat(response.count()).isZero();
    }

}