package br.unitins.tp1.projeto.service;

import java.time.LocalDate;
import java.util.List;

import br.unitins.tp1.projeto.dto.CupomDescontoRequestDTO;
import br.unitins.tp1.projeto.dto.CupomDescontoResponseDTO;
import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.CupomDesconto;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.repository.CupomDescontoRepository;
import br.unitins.tp1.projeto.repository.PamonhaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CupomDescontoServiceImpl implements CupomDescontoService {

    @Inject
    CupomDescontoRepository cupomDescontoRepository;

    @Inject
    PamonhaRepository pamonhaRepository;

    @Override
    @Transactional
    public CupomDescontoResponseDTO criar(CupomDescontoRequestDTO dto) {
        CupomDesconto cupom = new CupomDesconto();
        cupom.setCodigo(dto.codigo());
        cupom.setDescricao(dto.descricao());
        cupom.setValorDesconto(dto.valorDesconto());
        cupom.setDataValidade(dto.dataValidade());
        if (dto.pamonhaIds() != null) {
            for (Long pamonhaId : dto.pamonhaIds()) {
                Pamonha pamonha = pamonhaRepository.findById(pamonhaId);
                if (pamonha != null) cupom.getPamonhas().add(pamonha);
            }
        }
        cupomDescontoRepository.persist(cupom);
        return toResponseDTO(cupom);
    }

    @Override
    public List<CupomDescontoResponseDTO> listarTodos(int page, int size) {
        return cupomDescontoRepository.findAll().page(page, size).list().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public CupomDescontoResponseDTO validarCodigo(String codigo, List<Long> pamonhaIds) {
        CupomDesconto cupom = cupomDescontoRepository.findByCodigo(codigo);
        if (cupom == null || cupom.getDataValidade().isBefore(LocalDate.now())) {
            throw new ValidationException("codigo", "Cupom inválido ou expirado");
        }
        if (!cupom.getPamonhas().isEmpty()) {
            boolean temPamonhaValida = pamonhaIds != null && !pamonhaIds.isEmpty() &&
                    cupom.getPamonhas().stream().anyMatch(p -> pamonhaIds.contains(p.getId()));
            if (!temPamonhaValida) {
                throw new ValidationException("codigo", "Cupom não é válido para os itens selecionados");
            }
        }
        return toResponseDTO(cupom);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        cupomDescontoRepository.deleteById(id);
    }

    private CupomDescontoResponseDTO toResponseDTO(CupomDesconto c) {
        return new CupomDescontoResponseDTO(
            c.getId(), c.getCodigo(), c.getDescricao(), c.getValorDesconto(), c.getDataValidade()
        );
    }
}
