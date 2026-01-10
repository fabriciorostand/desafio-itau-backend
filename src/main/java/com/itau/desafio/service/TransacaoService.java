package com.itau.desafio.service;

import com.itau.desafio.dto.EstatisticaResponse;
import com.itau.desafio.dto.RegistrarTransacaoRequest;
import com.itau.desafio.entity.Transacao;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class TransacaoService {

    private final Queue<Transacao> transacoes = new ConcurrentLinkedQueue<>();

    public void registrar(RegistrarTransacaoRequest request) {
        Transacao transacao = new Transacao(request);

        transacoes.add(transacao);
    }

    public EstatisticaResponse buscarEstatisticas() {
        OffsetDateTime limite = OffsetDateTime.now().minusSeconds(60);

        DoubleSummaryStatistics estatisticas = transacoes.stream()
                .filter(t -> t.getDataHora().isAfter(limite))
                .mapToDouble(Transacao::getValor)
                .summaryStatistics();

        if (estatisticas.getCount() == 0) {
            return new EstatisticaResponse(0, 0, 0, 0, 0);
        }

        return new EstatisticaResponse(
                estatisticas.getCount(),
                estatisticas.getSum(),
                estatisticas.getAverage(),
                estatisticas.getMin(),
                estatisticas.getMax()
        );
    }

}