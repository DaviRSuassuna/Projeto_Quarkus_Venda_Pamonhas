package br.unitins.tp1.projeto.mapper;

import java.util.List;
import java.util.ArrayList;

import br.unitins.tp1.projeto.dto.ItemReceitaRequestDTO;
import br.unitins.tp1.projeto.dto.ItemReceitaResponseDTO;
import br.unitins.tp1.projeto.dto.PamonhaRequestDTO;
import br.unitins.tp1.projeto.dto.PamonhaResponseDTO;
import br.unitins.tp1.projeto.dto.ReceitaResponseDTO;
import br.unitins.tp1.projeto.model.ItemReceita;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.model.Receita;
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
        
        if (dto.receita() != null) {

            Receita receita = new Receita();
            receita.setDescricao(dto.receita().descricao());

            List<ItemReceita> itens = new ArrayList<>();

            for (ItemReceitaRequestDTO itemDTO : dto.receita().itens()) {
                
                ItemReceita item = new ItemReceita();

                item.setQuantidade(itemDTO.quantidade());
                item.setUnidadeMedida(itemDTO.unidadeMedida());

                itens.add(item);
            }

            receita.setItens(itens);
            pamonha.setReceita(receita);
        }

        return pamonha;
    }

    public static PamonhaResponseDTO toResponseDTO(Pamonha pamonha) {

        if(pamonha == null) return null;

        Receita receita = pamonha.getReceita();

        ReceitaResponseDTO receitaDTO = null;

        if (receita != null) {
            
            List<ItemReceitaResponseDTO> itensDTO = receita.getItens().stream()
                .map(item -> new ItemReceitaResponseDTO(
                    item.getQuantidade(),
                    item.getUnidadeMedida(),
                    item.getIngrediente().getNome()
                )).toList();

                receitaDTO = new ReceitaResponseDTO(
                    receita.getDescricao(),
                    itensDTO
                );
        }

        return new PamonhaResponseDTO(
            pamonha.getId(),
            pamonha.getNome(),
            pamonha.getDescricao(),
            pamonha.getPreco(),
            pamonha.getEstoque(),
            pamonha.getSaborPamonha(),
            pamonha.getTipoPamonha(),
            receitaDTO
        );
    }
}
