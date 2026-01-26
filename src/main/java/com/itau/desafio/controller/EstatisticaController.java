package com.itau.desafio.controller;

import com.itau.desafio.domain.estatistica.EstatisticaService;
import com.itau.desafio.dto.EstatisticaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estatistica")
@RequiredArgsConstructor
public class EstatisticaController {

    private final EstatisticaService service;

    @GetMapping
    @Operation(description = "Endpoint responsável por calcular estatísticas de transações")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca efetuada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na busca de estatísticas de transações"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<EstatisticaResponse> calcularEstatisticas(@RequestParam(name = "segundos", defaultValue = "60") @Positive Long segundosFiltragem) {
        EstatisticaResponse estatisticas = service.calcularEstatisticasTransacoes(segundosFiltragem);

        return ResponseEntity
                .ok()
                .body(estatisticas);
    }

}