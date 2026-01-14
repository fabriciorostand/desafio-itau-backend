package com.itau.desafio.controller;

import com.itau.desafio.dto.EstatisticaResponse;
import com.itau.desafio.dto.RegistrarTransacaoRequest;
import com.itau.desafio.service.TransacaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TransacaoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<RegistrarTransacaoRequest> registrarTransacaoRequestJson;

    @Autowired
    private JacksonTester<EstatisticaResponse> estatisticaResponseJson;

    @MockitoBean
    private TransacaoService service;

    @Test
    @DisplayName("Deveria devolver código http 201 quando informações estão válidas")
    void registrarCenario1() throws Exception {
        RegistrarTransacaoRequest registroRequest = new RegistrarTransacaoRequest(
                100,
                OffsetDateTime.parse("2026-01-11T23:39:00-03:00")
        );

        MockHttpServletResponse response = mvc.perform(post("/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrarTransacaoRequestJson.write(
                            registroRequest
                        ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 422 quando a transação não for aceita por qualquer motivo " +
            "(1 ou mais dos critérios de aceite não foram atendidos - por exemplo: uma transação com valor menor que 0")
    void registrarCenario2() throws Exception {
        RegistrarTransacaoRequest registroRequest = new RegistrarTransacaoRequest(
                -100,
                OffsetDateTime.parse("2026-01-11T23:39:00-03:00")
        );

        MockHttpServletResponse response = mvc.perform(post("/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrarTransacaoRequestJson.write(
                                registroRequest
                        ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando a API receber um JSON invalido")
    void registrarCenario3() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ valor: 100, }"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando há ausencia de body")
    void registrarCenario4() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/transacao"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e retornar um JSON com as estatisticas")
    void buscarEstatisticasCenario1() throws Exception {
        EstatisticaResponse estatisticaResponse = new EstatisticaResponse(
                1,
                100,
                100,
                100,
                100
        );

        when(service.buscarEstatisticas(any())).thenReturn(estatisticaResponse);

        MockHttpServletResponse response = mvc.perform(get("/transacao/estatistica")
                        .param("segundos", "60"))
                .andReturn().getResponse();

        var jsonEsperado = estatisticaResponseJson.write(
                estatisticaResponse
        ).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria devolver código http 200 quando há ausencia de query param")
    void buscarEstatisticasCenario2() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/transacao/estatistica"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 422 quando os segundos no query param não for positivo")
    void buscarEstatisticasCenario3() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/transacao/estatistica")
                        .param("segundos", "-60"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200")
    void limpar() throws Exception {
        MockHttpServletResponse response = mvc.perform(delete("/transacao"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}