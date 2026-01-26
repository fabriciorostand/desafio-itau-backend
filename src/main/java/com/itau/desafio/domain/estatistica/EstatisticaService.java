package com.itau.desafio.domain.estatistica;

import com.itau.desafio.domain.transacao.Transacao;
import com.itau.desafio.domain.transacao.TransacaoService;
import com.itau.desafio.dto.EstatisticaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstatisticaService {

    private final TransacaoService service;

    public EstatisticaResponse calcularEstatisticasTransacoes(Long segundosFiltragem) {
        long inicio = System.currentTimeMillis();

        log.debug("Calculando estatísticas - Filtro: {}s", segundosFiltragem);

        List<Transacao> transacoes = service.buscarTransacoes(segundosFiltragem);

        if (transacoes.isEmpty()) {
            log.info("Nenhuma transação encontrada no período de {}s", segundosFiltragem);
            return new EstatisticaResponse(0L, 0.0, 0.0, 0.0, 0.0);
        }

        DoubleSummaryStatistics estatisticas =  transacoes.stream()
                .mapToDouble(Transacao::getValor)
                .summaryStatistics();

        EstatisticaResponse response = new EstatisticaResponse(
                estatisticas.getCount(),
                estatisticas.getSum(),
                estatisticas.getAverage(),
                estatisticas.getMin(),
                estatisticas.getMax()
        );

        long duracao = System.currentTimeMillis() - inicio;

        log.info("Estatísticas calculadas [{}ms] - Count: {} | Sum: {} | Avg: {} | Min: {} | Max: {}",
                duracao, response.count(), response.sum(),
                response.avg(), response.min(), response.max());

        return response;
    }

}