package com.itau.desafio.domain.transacao;

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
import java.util.List;

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

        TransacaoMapper mapper = Mappers.getMapper(TransacaoMapper.class);


        service = new TransacaoService(clock, mapper);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houve transações registradas")
    void registrarCenario1() {
        List<Transacao> listaTransacoes = service.buscarTransacoes(60L);

        assertThat(listaTransacoes).isEmpty();
    }

    @Test
    @DisplayName("Deve registrar transações corretamente quando valores são válidos")
    void registrarCenario2() {
        OffsetDateTime dataHora1 = OffsetDateTime.now(clock).minusSeconds(10);
        OffsetDateTime dataHora2 = OffsetDateTime.now(clock).minusSeconds(60);

        service.registrar(new TransacaoRequest(200.0, dataHora1));
        service.registrar(new TransacaoRequest(300.0, dataHora2));

        List<Transacao> listaTransacoes = service.buscarTransacoes(60L);

        assertThat(listaTransacoes.size()).isEqualTo(2);
        assertThat(listaTransacoes.get(0).getValor()).isEqualTo(200);
        assertThat(listaTransacoes.get(0).getDataHora()).isEqualTo(dataHora1);
        assertThat(listaTransacoes.get(1).getValor()).isEqualTo(300);
        assertThat(listaTransacoes.get(1).getDataHora()).isEqualTo(dataHora2);
    }

    @Test
    @DisplayName("Deve limpar todas as transações")
    void limparCenario1() {
        service.registrar(new TransacaoRequest(200.0, OffsetDateTime.now(clock).minusSeconds(10)));
        service.registrar(new TransacaoRequest(100.0, OffsetDateTime.now(clock).minusSeconds(20)));

        service.limpar();

        List<Transacao> listaTransacoes = service.buscarTransacoes(60L);

        assertThat(listaTransacoes).isEmpty();
    }

}