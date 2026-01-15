package com.itau.desafio.service;

import com.itau.desafio.dto.EstatisticaResponse;
import com.itau.desafio.dto.RegistrarTransacaoRequest;
import com.itau.desafio.entity.Transacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final Queue<Transacao> transacoes = new ConcurrentLinkedQueue<>();
    private final Clock clock;

    public void registrar(RegistrarTransacaoRequest request) {
        Transacao transacao = new Transacao(request);

        transacoes.add(transacao);
    }

    public EstatisticaResponse buscarEstatisticas(Long segundosFiltragem) {
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

    private DoubleSummaryStatistics calcularEstatisticas(Long segundosFiltragem) {
        OffsetDateTime limite = OffsetDateTime.now(clock).minusSeconds(segundosFiltragem);

        return transacoes.stream()
                .filter(t -> !t.getDataHora().isBefore(limite))
                .mapToDouble(Transacao::getValor)
                .summaryStatistics();
    }

    public void limpar() {
        transacoes.clear();
    }

}