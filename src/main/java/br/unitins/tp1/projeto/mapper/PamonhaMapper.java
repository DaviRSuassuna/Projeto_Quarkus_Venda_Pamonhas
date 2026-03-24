package br.unitins.tp1.projeto.mapper;

import br.unitins.tp1.projeto.dto.PamonhaRequestDTO;
import br.unitins.tp1.projeto.dto.PamonhaResponseDTO;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.model.SaborPamonha;
import br.unitins.tp1.projeto.model.TipoPamonha;

public class PamonhaMapper {

    public static Pamonha toEntity(PamonhaRequestDTO dto) {
        
        if (dto == null) {
            return null;
        }

        Pamonha pamonha = new Pamonha();
        pamonha.setNome(dto.nome());
        pamonha.setDescricao(dto.descricao());;
        pamonha.setPreco(dto.preco());
        pamonha.setEstoque(dto.estoque());
        pamonha.setSaborPamonha(SaborPamonha.valueOf(dto.idSaborPamonha()));
        pamonha.setTipoPamonha(TipoPamonha.valueOf(dto.idTipoPamonha()));

        return pamonha;
    }

    public static PamonhaResponseDTO toResponseDTO(Pamonha pamonha) {

        if(pamonha == null) {
            return null;
        }

        return new PamonhaResponseDTO(
            pamonha.getId(),
            pamonha.getNome(),
            pamonha.getDescricao(),
            pamonha.getPreco(),
            pamonha.getEstoque(),
            pamonha.getSaborPamonha(),
            pamonha.getTipoPamonha()
        );
    }
}
