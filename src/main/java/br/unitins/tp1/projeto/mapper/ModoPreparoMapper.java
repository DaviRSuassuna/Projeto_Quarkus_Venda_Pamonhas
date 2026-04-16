package br.unitins.tp1.projeto.mapper;

import br.unitins.tp1.projeto.dto.ModoPreparoRequestDTO;
import br.unitins.tp1.projeto.dto.ModoPreparoResponseDTO;
import br.unitins.tp1.projeto.model.ModoPreparo;

public class ModoPreparoMapper {

    public static ModoPreparo toEntity(ModoPreparoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        ModoPreparo modo = new ModoPreparo();
        modo.setDescricao(dto.descricao());
        modo.setTempoPreparoMinutos(dto.tempoPreparoMinutos());
        return modo;
    }

    public static ModoPreparoResponseDTO toResponseDTO(ModoPreparo modo) {
        if (modo == null) {
            return null;
        }
        return new ModoPreparoResponseDTO(
            modo.getDescricao(),
            modo.getTempoPreparoMinutos()
        );
    }
}
