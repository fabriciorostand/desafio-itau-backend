package com.itau.desafio.domain.transacao;

import com.itau.desafio.dto.RegistrarTransacaoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransacaoService {

    private final List<Transacao> transacoes = new ArrayList<>();
    private final Clock clock;

    public void registrar(RegistrarTransacaoRequest request) {
        log.debug("Iniciando registro de transação: {}", request);
        Transacao transacao = new Transacao(request);

        transacoes.add(transacao);

        log.info("Transação registrada - Valor: {} | DataHora: {} | Total na fila: {}", transacao.getValor(), transacao.getDataHora(), transacoes.size());
    }


    public List<Transacao> buscarTransacoes(Long segundosFiltragem) {
        OffsetDateTime limite = OffsetDateTime.now(clock).minusSeconds(segundosFiltragem);
        log.debug("Buscando transações - Filtro: {}s - Limite de data: {}", segundosFiltragem, limite);

        List<Transacao> resultado = transacoes.stream()
                .filter(transacao -> !transacao.getDataHora().isBefore(limite)).toList();

        log.debug("Busca com filtro realizada - Total: {} | Incluídas: {} | Excluídas: {}",
                transacoes.size(),
                resultado.size(),
                transacoes.size() - resultado.size());

        return resultado;
    }

    public void limpar() {
        int quantidade = transacoes.size();
        log.warn("Iniciando limpeza de transações - Total: {}", quantidade);

        transacoes.clear();

        log.warn("Transações removidas - Total: {}", quantidade);
    }

}