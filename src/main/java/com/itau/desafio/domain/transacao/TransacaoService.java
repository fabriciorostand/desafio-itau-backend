package com.itau.desafio.domain.transacao;

import com.itau.desafio.dto.EstatisticaResponse;
import com.itau.desafio.dto.RegistrarTransacaoRequest;
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

        log.info("Transação registrada - Valor: {} | DataHora: {} | Total na fila: {}", transacao.getValor(), transacao.getDataHora(), transacoes.size());
    }


    public EstatisticaResponse buscarEstatisticas(Long segundosFiltragem) {
        long inicio = System.currentTimeMillis();

        log.debug("Buscando estatísticas - Filtro: {}s", segundosFiltragem);

        DoubleSummaryStatistics estatisticas = calcularEstatisticas(segundosFiltragem);

        if (estatisticas.getCount() == 0) {
            log.info("Nenhuma transação encontrada no período de {}s", segundosFiltragem);
            return new EstatisticaResponse(0, 0, 0, 0, 0);
        }

        long duracao = System.currentTimeMillis() - inicio;

        EstatisticaResponse response = new EstatisticaResponse(
                estatisticas.getCount(),
                estatisticas.getSum(),
                estatisticas.getAverage(),
                estatisticas.getMin(),
                estatisticas.getMax()
        );

        log.info("Estatísticas calculadas [{}ms] - Count: {} | Sum: {} | Avg: {} | Min: {} | Max: {}",
                duracao, response.count(), response.sum(), response.avg(),
                response.min(), response.max());

        return response;
    }

    private DoubleSummaryStatistics calcularEstatisticas(Long segundosFiltragem) {
        OffsetDateTime limite = OffsetDateTime.now(clock).minusSeconds(segundosFiltragem);
        log.debug("Calculando estatísticas com limite de data: {}", limite);

        long totalTransacoes = transacoes.size();

        DoubleSummaryStatistics estatisticas =  transacoes.stream()
                .filter(t -> !t.getDataHora().isBefore(limite))
                .mapToDouble(Transacao::getValor)
                .summaryStatistics();

        log.debug("Filtro aplicado - Total: {} | Incluídas: {} | Excluídas: {}",
                totalTransacoes, estatisticas.getCount(),
                totalTransacoes - estatisticas.getCount());

        return estatisticas;
    }

    public void limpar() {
        int quantidade = transacoes.size();
        log.warn("Iniciando limpeza de transações - Total: {}", quantidade);

        transacoes.clear();

        log.warn("Transações removidas - Total: {}", quantidade);
    }

}