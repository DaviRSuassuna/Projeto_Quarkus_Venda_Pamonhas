package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.CupomDescontoRequestDTO;
import br.unitins.tp1.projeto.dto.CupomDescontoResponseDTO;

public interface CupomDescontoService {

    CupomDescontoResponseDTO criar(CupomDescontoRequestDTO dto);

    List<CupomDescontoResponseDTO> listarTodos(int page, int size);

    CupomDescontoResponseDTO validarCodigo(String codigo);

    void deletar(Long id);
}
