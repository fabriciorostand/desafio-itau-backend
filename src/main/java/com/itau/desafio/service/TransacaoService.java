package com.itau.desafio.service;

import com.itau.desafio.dto.RegistrarTransacaoRequest;
import com.itau.desafio.entity.Transacao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransacaoService {

    private final List<Transacao> transacoes = new ArrayList<>();

    public void registrar(RegistrarTransacaoRequest request) {
        Transacao transacao = new Transacao(request);

        transacoes.add(transacao);
    }

}