package br.unitins.tp1.projeto.mapper;

import br.unitins.tp1.projeto.dto.EmbalagemRequestDTO;
import br.unitins.tp1.projeto.dto.EmbalagemResponseDTO;
import br.unitins.tp1.projeto.model.Embalagem;
import br.unitins.tp1.projeto.model.EmbalagemBiodegradavel;
import br.unitins.tp1.projeto.model.EmbalagemPlastica;

public class EmbalagemMapper {

    public static Embalagem toEntity(EmbalagemRequestDTO dto) {

        if ("PLASTICA".equalsIgnoreCase(dto.tipo())) {
            EmbalagemPlastica e = new EmbalagemPlastica();
            e.setDescricao(dto.descricao());
            e.setCusto(dto.custo());
            e.setPesoSuportado(dto.pesoSuportado());
            e.setTipoPlastico(dto.tipoPlastico());
            e.setReciclavel(dto.reciclavel() != null && dto.reciclavel());
            return e;
        }

        if ("BIODEGRADAVEL".equalsIgnoreCase(dto.tipo())) {
            EmbalagemBiodegradavel e = new EmbalagemBiodegradavel();
            e.setDescricao(dto.descricao());
            e.setCusto(dto.custo());
            e.setPesoSuportado(dto.pesoSuportado());
            e.setMaterial(dto.material());
            e.setTempoDecomposicaoDias(dto.tempoDecomposicaoDias());
            return e;
        }

        throw new RuntimeException("Tipo de embalagem inválido: " + dto.tipo());
    }

    public static EmbalagemResponseDTO toResponseDTO(Embalagem e) {

        if (e instanceof EmbalagemPlastica plastica) {
            return new EmbalagemResponseDTO(
                plastica.getId(),
                "PLASTICA",
                plastica.getDescricao(),
                plastica.getCusto(),
                plastica.getPesoSuportado(),
                plastica.getTipoPlastico(),
                plastica.isReciclavel(),
                null,
                null
            );
        }

        if (e instanceof EmbalagemBiodegradavel bio) {
            return new EmbalagemResponseDTO(
                bio.getId(),
                "BIODEGRADAVEL",
                bio.getDescricao(),
                bio.getCusto(),
                bio.getPesoSuportado(),
                null,
                null,
                bio.getMaterial(),
                bio.getTempoDecomposicaoDias()
            );
        }

        throw new RuntimeException("Tipo desconhecido");
    }
}