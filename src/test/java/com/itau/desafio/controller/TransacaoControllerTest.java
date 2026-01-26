package com.itau.desafio.controller;

import com.itau.desafio.dto.RegistrarTransacaoRequest;
import com.itau.desafio.domain.transacao.TransacaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(TransacaoController.class)
@AutoConfigureJsonTesters
class TransacaoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<RegistrarTransacaoRequest> registrarTransacaoRequestJson;

    @MockitoBean
    private TransacaoService service;

    @Test
    @DisplayName("Deveria devolver código http 201 quando informações estão válidas")
    void registrarCenario1() throws Exception {
        RegistrarTransacaoRequest registroRequest = new RegistrarTransacaoRequest(
                100.0,
                OffsetDateTime.now().minusSeconds(10)
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
    @DisplayName("Deveria devolver código http 422 quando o valor for negativo")
    void registrarCenario2() throws Exception {
        RegistrarTransacaoRequest registroRequest = new RegistrarTransacaoRequest(
                -100.0,
                OffsetDateTime.now().minusSeconds(10)
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
    @DisplayName("Deveria devolver código http 422 quando a data/hora for no futuro")
    void registrarCenario3() throws Exception {
        RegistrarTransacaoRequest registroRequest = new RegistrarTransacaoRequest(
                100.0,
                OffsetDateTime.now().plusSeconds(60)
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
    void registrarCenario4() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ valor: 100, }"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 400 quando há ausencia de body")
    void registrarCenario5() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/transacao"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200")
    void limpar() throws Exception {
        MockHttpServletResponse response = mvc.perform(delete("/transacao"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}