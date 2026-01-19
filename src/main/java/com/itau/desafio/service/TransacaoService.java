package com.itau.desafio.service;

import com.itau.desafio.dto.EstatisticaResponse;
import com.itau.desafio.dto.RegistrarTransacaoRequest;
import com.itau.desafio.entity.Transacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransacaoService {

    private final Queue<Transacao> transacoes = new ConcurrentLinkedQueue<>();
    private final Clock clock;

    public void registrar(RegistrarTransacaoRequest request) {
        log.debug("Iniciando registro de transação: {}", request);
        Transacao transacao = new Transacao(request);

        transacoes.add(transacao);

        log.info("Transação registrada com sucesso - Valor: {}, DataHora: {}", transacao.getValor(), transacao.getDataHora());
        log.debug("Total de transações na fila: {}", transacoes.size());
    }


    public EstatisticaResponse buscarEstatisticas(Long segundosFiltragem) {
        log.debug("Buscando estatísticas com filtro de {} segundos", segundosFiltragem);

        DoubleSummaryStatistics estatisticas = calcularEstatisticas(segundosFiltragem);

        if (estatisticas.getCount() == 0) {
            log.info("Nenhuma transação encontrada no período de {} segundos", segundosFiltragem);
            return new EstatisticaResponse(0, 0, 0, 0, 0);
        }

        EstatisticaResponse response = new EstatisticaResponse(
                estatisticas.getCount(),
                estatisticas.getSum(),
                estatisticas.getAverage(),
                estatisticas.getMin(),
                estatisticas.getMax()
        );

        log.info("Estatísticas calculadas - Count: {}, Sum: {}, Avg: {}, Min: {}, Max: {}",
                response.count(), response.sum(), response.avg(),
                response.min(), response.max());

        return response;
    }

    private DoubleSummaryStatistics calcularEstatisticas(Long segundosFiltragem) {
        OffsetDateTime limite = OffsetDateTime.now(clock).minusSeconds(segundosFiltragem);
        log.debug("Calculando estatísticas com limite de data: {}", limite);

        long totalTransacoes = transacoes.size();

        DoubleSummaryStatistics estatisticas =  transacoes.stream()
                .filter(t -> {
                    boolean dentroDoLimite = !t.getDataHora().isBefore(limite);

                    if (!dentroDoLimite) {
                        log.trace("Transação filtrada (fora do período): DataHora={}", t.getDataHora());
                    }

                    return dentroDoLimite;
                })
                .mapToDouble(Transacao::getValor)
                .summaryStatistics();

        log.debug("Filtradas {} de {} transações totais", estatisticas.getCount(), totalTransacoes);
        return estatisticas;
    }

    public void limpar() {
        int quantidadeAntes = transacoes.size();
        log.info("Iniciando limpeza de transações. Quantidade atual: {}", quantidadeAntes);

        transacoes.clear();
        log.info("Transações limpas com sucesso. {} transações removidas", quantidadeAntes);
    }

}