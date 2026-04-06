package br.unitins.tp1.projeto.mapper;

import br.unitins.tp1.projeto.dto.CupomDescontoRequestDTO;
import br.unitins.tp1.projeto.dto.CupomDescontoResponseDTO;
import br.unitins.tp1.projeto.model.CupomDesconto;

public class CupomDescontoMapper {

    public static CupomDesconto toEntity(CupomDescontoRequestDTO dto) {

        if (dto == null) return null;

        CupomDesconto cupomDesconto = new CupomDesconto();
        cupomDesconto.setCodigo(dto.codigo());
        cupomDesconto.setValorDesconto(dto.valorDesconto());
        cupomDesconto.setDataValidade(dto.dataValidade());
        cupomDesconto.setAtivo(dto.ativo());

        return cupomDesconto;
    }

    public static CupomDescontoResponseDTO toResponseDTO(CupomDesconto cupomDesconto) {

        if (cupomDesconto == null) return null;

        return new CupomDescontoResponseDTO(
            cupomDesconto.getId(),
            cupomDesconto.getCodigo(),
            cupomDesconto.getValorDesconto(),
            cupomDesconto.getDataValidade(),
            cupomDesconto.getAtivo()
        );
    }
}
