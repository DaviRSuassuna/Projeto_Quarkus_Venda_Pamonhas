package br.unitins.tp1.projeto.mapper;

import java.util.ArrayList;
import java.util.List;

import br.unitins.tp1.projeto.dto.CategoriaRequestDTO;
import br.unitins.tp1.projeto.dto.CategoriaResponseDTO;
import br.unitins.tp1.projeto.dto.EmbalagemRequestDTO;
import br.unitins.tp1.projeto.dto.EmbalagemResponseDTO;
import br.unitins.tp1.projeto.dto.ItemReceitaRequestDTO;
import br.unitins.tp1.projeto.dto.ItemReceitaResponseDTO;
import br.unitins.tp1.projeto.dto.ModoPreparoRequestDTO;
import br.unitins.tp1.projeto.dto.ModoPreparoResponseDTO;
import br.unitins.tp1.projeto.dto.PamonhaRequestDTO;
import br.unitins.tp1.projeto.dto.PamonhaResponseDTO;
import br.unitins.tp1.projeto.dto.TabelaNutricionalRequestDTO;
import br.unitins.tp1.projeto.dto.TabelaNutricionalResponseDTO;
import br.unitins.tp1.projeto.model.Categoria;
import br.unitins.tp1.projeto.model.Embalagem;
import br.unitins.tp1.projeto.model.EmbalagemBiodegradavel;
import br.unitins.tp1.projeto.model.EmbalagemPlastica;
import br.unitins.tp1.projeto.model.Ingrediente;
import br.unitins.tp1.projeto.model.ItemReceita;
import br.unitins.tp1.projeto.model.ModoPreparo;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.model.TabelaNutricional;

public class PamonhaMapper {

    public static Pamonha toEntity(PamonhaRequestDTO dto) {

        if (dto == null) {
            return null;
        }

        Pamonha pamonha = new Pamonha();
        pamonha.setNome(dto.nome());
        pamonha.setDescricao(dto.descricao());
        pamonha.setPreco(dto.preco());
        pamonha.setEstoque(dto.estoque());

        if (dto.tabelaNutricional() != null) {
            pamonha.setTabelaNutricional(toEntity(dto.tabelaNutricional()));
        }

        if (dto.modoPreparo() != null) {
            pamonha.setModoPreparo(toEntity(dto.modoPreparo()));
        }

        if (dto.embalagem() != null) {
            pamonha.setEmbalagem(toEntity(dto.embalagem()));
        }

        if (dto.categorias() != null) {
            List<Categoria> categorias = new ArrayList<>();
            for (CategoriaRequestDTO categoriaDTO : dto.categorias()) {
                categorias.add(toEntity(categoriaDTO));
            }
            pamonha.setCategorias(categorias);
        }

        if (dto.itensReceita() != null) {
            List<ItemReceita> itens = new ArrayList<>();
            for (ItemReceitaRequestDTO itemDTO : dto.itensReceita()) {
                ItemReceita item = new ItemReceita();
                item.setQuantidade(itemDTO.quantidade());
                item.setUnidadeMedida(itemDTO.unidadeMedida());
                if (itemDTO.ingredienteId() != null) {
                    Ingrediente ingrediente = new Ingrediente();
                    ingrediente.setId(itemDTO.ingredienteId());
                    item.setIngrediente(ingrediente);
                }
                itens.add(item);
            }
            pamonha.setItensReceita(itens);
        }

        return pamonha;
    }

    public static PamonhaResponseDTO toResponseDTO(Pamonha pamonha) {

        if (pamonha == null) {
            return null;
        }

        List<CategoriaResponseDTO> categorias = pamonha.getCategorias().stream()
            .map(PamonhaMapper::toResponseDTO)
            .toList();

        List<ItemReceitaResponseDTO> itensReceita = pamonha.getItensReceita().stream()
            .map(item -> new ItemReceitaResponseDTO(
                item.getQuantidade(),
                item.getUnidadeMedida(),
                item.getIngrediente() != null ? item.getIngrediente().getNome() : null
            ))
            .toList();

        return new PamonhaResponseDTO(
            pamonha.getId(),
            pamonha.getNome(),
            pamonha.getDescricao(),
            pamonha.getPreco(),
            pamonha.getEstoque(),
            toResponseDTO(pamonha.getTabelaNutricional()),
            toResponseDTO(pamonha.getModoPreparo()),
            toResponseDTO(pamonha.getEmbalagem()),
            categorias,
            itensReceita
        );
    }

    public static TabelaNutricional toEntity(TabelaNutricionalRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        TabelaNutricional tabela = new TabelaNutricional();
        tabela.setValorEnergetico(dto.valorEnergetico());
        tabela.setCarboidratos(dto.carboidratos());
        tabela.setProteinas(dto.proteinas());
        tabela.setGordurasTotais(dto.gordurasTotais());
        tabela.setFibras(dto.fibras());
        tabela.setSodio(dto.sodio());
        return tabela;
    }

    private static TabelaNutricionalResponseDTO toResponseDTO(TabelaNutricional tabela) {
        if (tabela == null) {
            return null;
        }
        return new TabelaNutricionalResponseDTO(
            tabela.getValorEnergetico(),
            tabela.getCarboidratos(),
            tabela.getProteinas(),
            tabela.getGordurasTotais(),
            tabela.getFibras(),
            tabela.getSodio()
        );
    }

    public static ModoPreparo toEntity(ModoPreparoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        ModoPreparo modo = new ModoPreparo();
        modo.setDescricao(dto.descricao());
        modo.setTempoPreparoMinutos(dto.tempoPreparoMinutos());
        return modo;
    }

    private static ModoPreparoResponseDTO toResponseDTO(ModoPreparo modo) {
        if (modo == null) {
            return null;
        }
        return new ModoPreparoResponseDTO(
            modo.getDescricao(),
            modo.getTempoPreparoMinutos()
        );
    }

    public static Embalagem toEntity(EmbalagemRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        if ("BIODEGRADAVEL".equalsIgnoreCase(dto.tipo())) {
            EmbalagemBiodegradavel embalagem = new EmbalagemBiodegradavel();
            embalagem.setDescricao(dto.descricao());
            embalagem.setCusto(dto.custo());
            embalagem.setPesoSuportado(dto.pesoSuportado());
            embalagem.setMaterial(dto.material());
            embalagem.setTempoDecomposicaoDias(dto.tempoDecomposicaoDias() != null ? dto.tempoDecomposicaoDias() : 0);
            return embalagem;
        }
        EmbalagemPlastica embalagem = new EmbalagemPlastica();
        embalagem.setDescricao(dto.descricao());
        embalagem.setCusto(dto.custo());
        embalagem.setPesoSuportado(dto.pesoSuportado());
        embalagem.setTipoPlastico(dto.tipoPlastico());
        embalagem.setReciclavel(dto.reciclavel() != null && dto.reciclavel());
        return embalagem;
    }

    private static EmbalagemResponseDTO toResponseDTO(Embalagem embalagem) {
        if (embalagem == null) {
            return null;
        }

        if (embalagem instanceof EmbalagemPlastica plastica) {
            return new EmbalagemResponseDTO(
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

        if (embalagem instanceof EmbalagemBiodegradavel biodegradavel) {
            return new EmbalagemResponseDTO(
                "BIODEGRADAVEL",
                biodegradavel.getDescricao(),
                biodegradavel.getCusto(),
                biodegradavel.getPesoSuportado(),
                null,
                null,
                biodegradavel.getMaterial(),
                biodegradavel.getTempoDecomposicaoDias()
            );
        }

        return new EmbalagemResponseDTO(
            "UNKNOWN",
            embalagem.getDescricao(),
            embalagem.getCusto(),
            embalagem.getPesoSuportado(),
            null,
            null,
            null,
            null
        );
    }

    public static Categoria toEntity(CategoriaRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        return categoria;
    }

    private static CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        return new CategoriaResponseDTO(
            categoria.getId(),
            categoria.getNome(),
            categoria.getDescricao()
        );
    }
}

