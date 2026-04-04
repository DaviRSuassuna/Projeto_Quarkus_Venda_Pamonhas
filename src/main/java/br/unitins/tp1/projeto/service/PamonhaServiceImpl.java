package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.ItemReceitaRequestDTO;
import br.unitins.tp1.projeto.dto.PamonhaRequestDTO;
import br.unitins.tp1.projeto.dto.PamonhaResponseDTO;
import br.unitins.tp1.projeto.mapper.PamonhaMapper;
import br.unitins.tp1.projeto.model.Ingrediente;
import br.unitins.tp1.projeto.model.ItemReceita;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.model.Receita;
import br.unitins.tp1.projeto.model.SaborPamonha;
import br.unitins.tp1.projeto.model.TipoPamonha;
import br.unitins.tp1.projeto.repository.IngredienteRepository;
import br.unitins.tp1.projeto.repository.PamonhaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PamonhaServiceImpl implements PamonhaService{

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
    public List<Pamonha> findBySaborPamonha(String saborPamonha) {
        return pamonhaRepository.findBySaborPamonha(saborPamonha);
    }

    @Override
    public List<Pamonha> findByTipoPamonha(String tipoPamonha) {
        return pamonhaRepository.findByTipoPamonha(tipoPamonha);
    }

    private void montarReceita(Pamonha pamonha, PamonhaRequestDTO dto) {
        
        if (dto.receita() == null) return;

        Receita receita = pamonha.getReceita();

        for (int i = 0; i < receita.getItens().size(); i++) {
            
            ItemReceita item = receita.getItens().get(i);
            ItemReceitaRequestDTO itemDTO = dto.receita().itens().get(i);

            Ingrediente ingrediente = ingredienteRepository.findById(itemDTO.ingredienteId());

            if (ingrediente == null) {
                throw new RuntimeException("Ingrediente não encontrado...");
            }

            item.setIngrediente(ingrediente);
            item.setReceita(receita);
        }
    }

    @Override
    @Transactional
    public PamonhaResponseDTO create(PamonhaRequestDTO dto) {
        Pamonha pamonha = PamonhaMapper.toEntity(dto);

        montarReceita(pamonha, dto);

        pamonha.setTabelaNutricional(
            pamonha.getReceita().calcularTabelaNutricional()
        );

        pamonhaRepository.persist(pamonha);
        
        return PamonhaMapper.toResponseDTO(pamonha);
    }

    @Override
    @Transactional
    public void update(Long id, PamonhaRequestDTO dto) {
        Pamonha pamonhaUpdate = findById(id);
        pamonhaUpdate.setNome(dto.nome());
        pamonhaUpdate.setDescricao(dto.descricao());
        pamonhaUpdate.setPreco(dto.preco());
        pamonhaUpdate.setEstoque(dto.estoque());
        pamonhaUpdate.setSaborPamonha(SaborPamonha.valueOf(dto.idSaborPamonha()));
        pamonhaUpdate.setTipoPamonha(TipoPamonha.valueOf(dto.idTipoPamonha()));
        pamonhaRepository.persist(pamonhaUpdate);      
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        pamonhaRepository.deleteById(id);       
    } 
}
