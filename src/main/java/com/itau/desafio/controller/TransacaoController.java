package com.itau.desafio.controller;

import com.itau.desafio.dto.RegistrarTransacaoRequest;
import com.itau.desafio.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacao")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService service;

    @PostMapping
    public ResponseEntity<Void> registrar(@RequestBody @Valid RegistrarTransacaoRequest request) {
        service.registrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

}
