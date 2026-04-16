package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.CategoriaRequestDTO;
import br.unitins.tp1.projeto.dto.ItemReceitaRequestDTO;
import br.unitins.tp1.projeto.dto.PamonhaRequestDTO;
import br.unitins.tp1.projeto.dto.PamonhaResponseDTO;
import br.unitins.tp1.projeto.mapper.PamonhaMapper;
import br.unitins.tp1.projeto.model.Embalagem;
import br.unitins.tp1.projeto.model.EmbalagemBiodegradavel;
import br.unitins.tp1.projeto.model.EmbalagemPlastica;
import br.unitins.tp1.projeto.model.Ingrediente;
import br.unitins.tp1.projeto.model.ItemReceita;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.repository.IngredienteRepository;
import br.unitins.tp1.projeto.repository.PamonhaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PamonhaServiceImpl implements PamonhaService {

    @Inject
    PamonhaRepository pamonhaRepository;

    @Inject
    IngredienteRepository ingredienteRepository;

    @Override
    public List<Pamonha> findAll() {
        return pamonhaRepository.findAll().list();
    }

    @Override
    public Pamonha findById(Long id) {
        return pamonhaRepository.findById(id);
    }

    @Override
    public List<Pamonha> findByNome(String nome) {
        return pamonhaRepository.findByNome(nome);
    }

    @Override
    public List<Pamonha> findByCategoria(Long categoriaId) {
        return pamonhaRepository.findByCategoria(categoriaId);
    }

    private void resolveReceitaIngredientes(Pamonha pamonha) {
        if (pamonha == null || pamonha.getItensReceita() == null) {
            return;
        }

        for (ItemReceita item : pamonha.getItensReceita()) {
            if (item.getIngrediente() == null || item.getIngrediente().getId() == null) {
                continue;
            }

            Ingrediente ingrediente = ingredienteRepository.findById(item.getIngrediente().getId());
            if (ingrediente == null) {
                throw new RuntimeException("Ingrediente não encontrado: " + item.getIngrediente().getId());
            }
            item.setIngrediente(ingrediente);
            item.setPamonha(pamonha);
        }
    }

    @Override
    @Transactional
    public PamonhaResponseDTO create(PamonhaRequestDTO dto) {
        Pamonha pamonha = PamonhaMapper.toEntity(dto);
        resolveReceitaIngredientes(pamonha);
        pamonhaRepository.persist(pamonha);
        return PamonhaMapper.toResponseDTO(pamonha);
    }

    @Override
    @Transactional
    public void update(Long id, PamonhaRequestDTO dto) {
        Pamonha pamonhaUpdate = findById(id);
        if (pamonhaUpdate == null) {
            throw new RuntimeException("Pamonha não encontrada: " + id);
        }

        pamonhaUpdate.setNome(dto.nome());
        pamonhaUpdate.setDescricao(dto.descricao());
        pamonhaUpdate.setPreco(dto.preco());
        pamonhaUpdate.setEstoque(dto.estoque());

        if (dto.tabelaNutricional() != null) {
            pamonhaUpdate.setTabelaNutricional(PamonhaMapper.toEntity(dto.tabelaNutricional()));
        }

        if (dto.modoPreparo() != null) {
            if (pamonhaUpdate.getModoPreparo() == null) {
                pamonhaUpdate.setModoPreparo(PamonhaMapper.toEntity(dto.modoPreparo()));
            } else {
                pamonhaUpdate.getModoPreparo().setDescricao(dto.modoPreparo().descricao());
                pamonhaUpdate.getModoPreparo().setTempoPreparoMinutos(dto.modoPreparo().tempoPreparoMinutos());
            }
        }

        if (dto.embalagem() != null) {
            if (pamonhaUpdate.getEmbalagem() == null || !pamonhaUpdate.getEmbalagem().getClass().getSimpleName().equalsIgnoreCase(dto.embalagem().tipo())) {
                pamonhaUpdate.setEmbalagem(PamonhaMapper.toEntity(dto.embalagem()));
            } else {
                Embalagem existing = pamonhaUpdate.getEmbalagem();
                existing.setDescricao(dto.embalagem().descricao());
                existing.setCusto(dto.embalagem().custo());
                existing.setPesoSuportado(dto.embalagem().pesoSuportado());
                if (existing instanceof EmbalagemPlastica plastica) {
                    plastica.setTipoPlastico(dto.embalagem().tipoPlastico());
                    plastica.setReciclavel(dto.embalagem().reciclavel() != null && dto.embalagem().reciclavel());
                } else if (existing instanceof EmbalagemBiodegradavel biodegradavel) {
                    biodegradavel.setMaterial(dto.embalagem().material());
                    biodegradavel.setTempoDecomposicaoDias(dto.embalagem().tempoDecomposicaoDias() != null ? dto.embalagem().tempoDecomposicaoDias() : 0);
                }
            }
        }

        if (dto.categorias() != null) {
            pamonhaUpdate.getCategorias().clear();
            for (CategoriaRequestDTO categoriaDTO : dto.categorias()) {
                pamonhaUpdate.getCategorias().add(PamonhaMapper.toEntity(categoriaDTO));
            }
        }

        if (dto.itensReceita() != null) {
            pamonhaUpdate.getItensReceita().clear();
            for (ItemReceitaRequestDTO itemDTO : dto.itensReceita()) {
                ItemReceita item = new ItemReceita();
                item.setQuantidade(itemDTO.quantidade());
                item.setUnidadeMedida(itemDTO.unidadeMedida());
                if (itemDTO.ingredienteId() != null) {
                    Ingrediente ingrediente = new Ingrediente();
                    ingrediente.setId(itemDTO.ingredienteId());
                    item.setIngrediente(ingrediente);
                }
                pamonhaUpdate.getItensReceita().add(item);
            }
            resolveReceitaIngredientes(pamonhaUpdate);
        }

        pamonhaRepository.persist(pamonhaUpdate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        pamonhaRepository.deleteById(id);
    }
}

