package com.itau.desafio.controller;

import com.itau.desafio.domain.estatistica.EstatisticaService;
import com.itau.desafio.dto.EstatisticaResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(EstatisticaController.class)
@AutoConfigureJsonTesters
class EstatisticaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<EstatisticaResponse> estatisticaResponseJson;

    @MockitoBean
    private EstatisticaService service;

    @Test
    @DisplayName("Deveria devolver código http 200 e retornar um JSON com as estatisticas")
    void buscarEstatisticasCenario1() throws Exception {
        EstatisticaResponse estatisticaResponse = new EstatisticaResponse(
                1L,
                100.0,
                100.0,
                100.0,
                100.0
        );

        when(service.calcularEstatisticasTransacoes(any())).thenReturn(estatisticaResponse);

        MockHttpServletResponse response = mvc.perform(get("/estatistica")
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
        MockHttpServletResponse response = mvc.perform(get("/estatistica"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 422 quando os segundos no query param não for positivo")
    void buscarEstatisticasCenario3() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/estatistica")
                        .param("segundos", "-60"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

}