package com.itau.desafio.mapstruct;

import com.itau.desafio.domain.transacao.Transacao;
import com.itau.desafio.dto.TransacaoRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransacaoMapper {

    Transacao paraEntidade(TransacaoRequest dto);
    TransacaoRequest paraRegistrarResquest(Transacao entity);

}