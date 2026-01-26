package com.itau.desafio.domain.transacao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transacao {

    private Double valor;
    private OffsetDateTime dataHora;

}