package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.CidadeRequestDTO;
import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.Cidade;
import br.unitins.tp1.projeto.model.Estado;
import br.unitins.tp1.projeto.repository.CidadeRepository;
import br.unitins.tp1.projeto.repository.EstadoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CidadeServiceImpl implements CidadeService {

    @Inject
    CidadeRepository cidadeRepository;

    @Inject
    EstadoRepository estadoRepository;

    @Override
    public List<Cidade> findAll() {
        return cidadeRepository.listAll();
    }

    @Override
    public Cidade findById(Long id) {
        return cidadeRepository.findById(id);
    }

    @Override
    public List<Cidade> findByEstado(Long estadoId) {
        return cidadeRepository.findByEstado(estadoId);
    }

    @Override
    @Transactional
    public Cidade create(CidadeRequestDTO dto) {
        Estado estado = estadoRepository.findById(dto.estadoId());
        if (estado == null) throw new ValidationException("estadoId", "Estado não encontrado");
        Cidade cidade = new Cidade();
        cidade.setNome(dto.nome());
        cidade.setEstado(estado);
        cidadeRepository.persist(cidade);
        return cidade;
    }

    @Override
    @Transactional
    public Cidade update(Long id, CidadeRequestDTO dto) {
        Cidade cidade = cidadeRepository.findById(id);
        if (cidade == null) throw new NotFoundException("Cidade não encontrada");
        cidade.setNome(dto.nome());
        if (dto.estadoId() != null) {
            Estado estado = estadoRepository.findById(dto.estadoId());
            if (estado == null) throw new ValidationException("estadoId", "Estado não encontrado");
            cidade.setEstado(estado);
        }
        return cidade;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Cidade cidade = cidadeRepository.findById(id);
        if (cidade == null) throw new NotFoundException("Cidade não encontrada");
        cidadeRepository.delete(cidade);
    }
}
