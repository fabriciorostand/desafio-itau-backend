package com.itau.desafio.domain.transacao;

import com.itau.desafio.dto.RegistrarTransacaoRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Transacao {

    private Double valor;
    private OffsetDateTime dataHora;

    // Construtor para converter RegistrarTransacaoRequest (DTO) para Transacao (Entidade)
    public Transacao(RegistrarTransacaoRequest request) {
        this.valor = request.valor();
        this.dataHora = request.dataHora();
    }

}