package com.itau.desafio.controller;

import com.itau.desafio.dto.EstatisticaResponse;
import com.itau.desafio.dto.RegistrarTransacaoRequest;
import com.itau.desafio.service.TransacaoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacao")
@Slf4j
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService service;

    @PostMapping
    public ResponseEntity<Void> registrar(@RequestBody @Valid RegistrarTransacaoRequest request) {
        log.debug("Registrando transação: {}", request);

        service.registrar(request);

        log.info("Transação registrada com sucesso");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/estatistica")
    public ResponseEntity<EstatisticaResponse> buscarEstatisticas(@RequestParam(name = "segundos", defaultValue = "60") @Positive Long segundosFiltragem) {
        log.debug("Buscando estatísticas com filtro de {} segundos", segundosFiltragem);

        EstatisticaResponse estatisticas = service.buscarEstatisticas(segundosFiltragem);

        log.debug("Estatísticas retornadas: {}", estatisticas);
        return ResponseEntity
                .ok()
                .body(estatisticas);
    }

    @DeleteMapping
    public ResponseEntity<Void> limpar() {
        log.warn("Limpando todas as transações");

        service.limpar();

        log.warn("Todas as transações foram removidas");
        return ResponseEntity
                .ok()
                .build();
    }

}
