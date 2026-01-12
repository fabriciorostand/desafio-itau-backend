package com.itau.desafio.service;

import com.itau.desafio.dto.EstatisticaResponse;
import com.itau.desafio.dto.RegistrarTransacaoRequest;
import com.itau.desafio.dto.SegundosFiltragem;
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

    public EstatisticaResponse buscarEstatisticas(SegundosFiltragem segundosFiltragem) {
        DoubleSummaryStatistics estatisticas = calcularEstatisticas(segundosFiltragem);

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

    private DoubleSummaryStatistics calcularEstatisticas(SegundosFiltragem segundosFiltragem) {
        OffsetDateTime limite = OffsetDateTime.now().minusSeconds(segundosFiltragem.segundos());

        return transacoes.stream()
                .filter(t -> t.getDataHora().isAfter(limite))
                .mapToDouble(Transacao::getValor)
                .summaryStatistics();
    }

    public void limpar() {
        transacoes.clear();
    }

}