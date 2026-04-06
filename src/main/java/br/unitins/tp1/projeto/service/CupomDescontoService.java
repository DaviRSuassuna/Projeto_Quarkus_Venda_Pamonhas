package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.CupomDescontoRequestDTO;
import br.unitins.tp1.projeto.model.CupomDesconto;

public interface CupomDescontoService {

    List<CupomDesconto> findAll();

    CupomDesconto findById(Long id);

    List<CupomDesconto> findByAtivo();

    List<CupomDesconto> listByValorDescontoCrescente();

    List<CupomDesconto> listByValorDescontoDecrescente();

    CupomDesconto create(CupomDesconto dto);

    void update(Long id, CupomDescontoRequestDTO dto);

    void delete(Long id);
}
