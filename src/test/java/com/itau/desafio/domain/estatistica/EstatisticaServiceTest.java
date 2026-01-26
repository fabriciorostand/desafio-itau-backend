package com.itau.desafio.domain.estatistica;

import com.itau.desafio.domain.transacao.TransacaoService;
import com.itau.desafio.dto.EstatisticaResponse;
import com.itau.desafio.dto.TransacaoRequest;
import com.itau.desafio.mapstruct.TransacaoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class EstatisticaServiceTest {

    private EstatisticaService estatisticaService;

    private TransacaoService transacaoService;

    private Clock clock;

    @BeforeEach
    void setService() {
        clock = Clock.fixed(
                Instant.parse("2026-01-15T12:00:00Z"),
                ZoneOffset.UTC
        );

        TransacaoMapper mapper = Mappers.getMapper(TransacaoMapper.class);

        transacaoService = new TransacaoService(clock, mapper);
        estatisticaService = new EstatisticaService(transacaoService);
    }

    @Test
    @DisplayName("Deve retornar estatísticas zeradas quando não houver transações")
    void calcularEstatisticasTransacoesCenario1() {
        EstatisticaResponse response = estatisticaService.calcularEstatisticasTransacoes(60L);

        assertThat(response.count()).isZero();
        assertThat(response.sum()).isZero();
        assertThat(response.avg()).isZero();
        assertThat(response.min()).isZero();
        assertThat(response.max()).isZero();
    }

    @Test
    @DisplayName("Deve retornar estatísticas corretamente quando houver apenas uma transação")
    void calcularEstatisticasTransacoesCenario2() {
        transacaoService.registrar(new TransacaoRequest(500.0, OffsetDateTime.now(clock).minusSeconds(10)));

        EstatisticaResponse response = estatisticaService.calcularEstatisticasTransacoes(60L);

        assertThat(response.count()).isEqualTo(1);
        assertThat(response.sum()).isEqualTo(500.0);
        assertThat(response.avg()).isEqualTo(500.0);
        assertThat(response.min()).isEqualTo(500.0);
        assertThat(response.max()).isEqualTo(500.0);
    }

    @Test
    @DisplayName("Deve calcular estatísticas corretamente para transações dentro do intervalo")
    void calcularEstatisticasTransacoesCenario3() {
        transacaoService.registrar(new TransacaoRequest(200.0, OffsetDateTime.now(clock).minusSeconds(10)));
        transacaoService.registrar(new TransacaoRequest(100.0, OffsetDateTime.now(clock).minusSeconds(20)));

        EstatisticaResponse response = estatisticaService.calcularEstatisticasTransacoes(60L);

        assertThat(response.count()).isEqualTo(2);
        assertThat(response.sum()).isEqualTo(300.0);
        assertThat(response.avg()).isEqualTo(150.0);
        assertThat(response.min()).isEqualTo(100.0);
        assertThat(response.max()).isEqualTo(200.0);
    }

    @Test
    @DisplayName("Deve processar transações com valor zero")
    void calcularEstatisticasTransacoesCenario4() {
        transacaoService.registrar(new TransacaoRequest(0.0, OffsetDateTime.now(clock).minusSeconds(30)));
        transacaoService.registrar(new TransacaoRequest(100.0, OffsetDateTime.now(clock).minusSeconds(40)));

        EstatisticaResponse response = estatisticaService.calcularEstatisticasTransacoes(60L);

        assertThat(response.count()).isEqualTo(2);
        assertThat(response.sum()).isEqualTo(100.0);
        assertThat(response.avg()).isEqualTo(50.0);
        assertThat(response.min()).isEqualTo(0.0);
        assertThat(response.max()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Deve calcular estatísticas corretamente para transações no limite exato do intervalo")
    void calcularEstatisticasTransacoesCenario5() {
        transacaoService.registrar(new TransacaoRequest(200.0, OffsetDateTime.now(clock).minusSeconds(60)));
        transacaoService.registrar(new TransacaoRequest(100.0, OffsetDateTime.now(clock).minusSeconds(60)));

        EstatisticaResponse response = estatisticaService.calcularEstatisticasTransacoes(60L);

        assertThat(response.count()).isEqualTo(2);
        assertThat(response.sum()).isEqualTo(300.0);
        assertThat(response.avg()).isEqualTo(150.0);
        assertThat(response.min()).isEqualTo(100.0);
        assertThat(response.max()).isEqualTo(200.0);
    }

    @Test
    @DisplayName("Deve ignorar transações fora do intervalo de tempo")
    void calcularEstatisticasTransacoesCenario6() {
        transacaoService.registrar(new TransacaoRequest(200.0, OffsetDateTime.now(clock).minusSeconds(40)));
        transacaoService.registrar(new TransacaoRequest(100.0, OffsetDateTime.now(clock).minusSeconds(50)));
        transacaoService.registrar(new TransacaoRequest(50.0, OffsetDateTime.now(clock).minusSeconds(80)));

        EstatisticaResponse response = estatisticaService.calcularEstatisticasTransacoes(60L);

        assertThat(response.count()).isEqualTo(2);
        assertThat(response.sum()).isEqualTo(300.0);
        assertThat(response.avg()).isEqualTo(150.0);
        assertThat(response.min()).isEqualTo(100.0);
        assertThat(response.max()).isEqualTo(200.0);
    }

    @Test
    @DisplayName("Deve retornar estatísticas zeradas quando todas as transações estiverem fora do intervalo")
    void calcularEstatisticasTransacoesCenario7() {
        transacaoService.registrar(new TransacaoRequest(200.0, OffsetDateTime.now(clock).minusSeconds(90)));
        transacaoService.registrar(new TransacaoRequest(100.0, OffsetDateTime.now(clock).minusSeconds(100)));

        EstatisticaResponse response = estatisticaService.calcularEstatisticasTransacoes(60L);

        assertThat(response.count()).isZero();
        assertThat(response.sum()).isZero();
        assertThat(response.avg()).isZero();
        assertThat(response.min()).isZero();
        assertThat(response.max()).isZero();
    }

}