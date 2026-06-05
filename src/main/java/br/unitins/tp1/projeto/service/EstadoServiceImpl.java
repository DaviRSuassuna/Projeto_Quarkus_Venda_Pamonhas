package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.EstadoRequestDTO;
import br.unitins.tp1.projeto.model.Estado;
import br.unitins.tp1.projeto.repository.EstadoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class EstadoServiceImpl implements EstadoService {

    @Inject
    EstadoRepository estadoRepository;

    @Override
    public List<Estado> findAll() {
        return estadoRepository.listAll();
    }

    @Override
    public Estado findById(Long id) {
        return estadoRepository.findById(id);
    }

    @Override
    @Transactional
    public Estado create(EstadoRequestDTO dto) {
        Estado estado = new Estado();
        estado.setNome(dto.nome());
        estado.setSigla(dto.sigla());
        estadoRepository.persist(estado);
        return estado;
    }

    @Override
    @Transactional
    public Estado update(Long id, EstadoRequestDTO dto) {
        Estado estado = estadoRepository.findById(id);
        if (estado == null) throw new NotFoundException("Estado não encontrado");
        estado.setNome(dto.nome());
        estado.setSigla(dto.sigla());
        return estado;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Estado estado = estadoRepository.findById(id);
        if (estado == null) throw new NotFoundException("Estado não encontrado");
        estadoRepository.delete(estado);
    }
}
