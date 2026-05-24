package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.EmbalagemRequestDTO;
import br.unitins.tp1.projeto.exception.ValidationException;
import br.unitins.tp1.projeto.model.Embalagem;
import br.unitins.tp1.projeto.model.EmbalagemBiodegradavel;
import br.unitins.tp1.projeto.model.EmbalagemPlastica;
import br.unitins.tp1.projeto.repository.EmbalagemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class EmbalagemServiceImpl implements EmbalagemService {

    @Inject
    EmbalagemRepository repository;

    @Override
    public List<Embalagem> findAll(int page, int size) {
        return repository.findAll().page(page, size).list();
    }

    @Override
    public Embalagem findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Embalagem> findByTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    @Override
    public List<Embalagem> findOrderByCusto() {
        return repository.findOrderByCusto();
    }

    @Override
    public List<Embalagem> findBiodegradavelRapida(int dias) {
        return repository.findBiodegradavelRapida(dias);
    }

    @Override
    @Transactional
    public Embalagem create(EmbalagemRequestDTO dto) {
        // Validação de negócio: verificar se a descrição já existe
        List<Embalagem> existentes = repository.findByDescricao(dto.descricao());
        if (!existentes.isEmpty()) {
            throw new ValidationException("descricao", "Já existe uma embalagem com esta descrição");
        }

        Embalagem embalagem;

        if ("PLASTICA".equalsIgnoreCase(dto.tipo())) {
            EmbalagemPlastica e = new EmbalagemPlastica();
            e.setTipoPlastico(dto.tipoPlastico());
            e.setReciclavel(dto.reciclavel() != null && dto.reciclavel());
            embalagem = e;

        } else if ("BIODEGRADAVEL".equalsIgnoreCase(dto.tipo())) {
            EmbalagemBiodegradavel e = new EmbalagemBiodegradavel();
            e.setMaterial(dto.material());
            e.setTempoDecomposicaoDias(dto.tempoDecomposicaoDias());
            embalagem = e;

        } else {
            throw new RuntimeException("Tipo inválido");
        }

        // campos comuns
        embalagem.setDescricao(dto.descricao());
        embalagem.setCusto(dto.custo());
        embalagem.setPesoSuportado(dto.pesoSuportado());

        repository.persist(embalagem);
        return embalagem;
    }

    @Override
    @Transactional
    public void update(Long id, EmbalagemRequestDTO dto) {
        Embalagem embalagem = findById(id);
        if (embalagem == null) {
            throw new RuntimeException("Embalagem não encontrada: " + id);
        }

        // Validação de negócio: verificar se a descrição já existe (exceto para o próprio)
        List<Embalagem> existentes = repository.findByDescricao(dto.descricao());
        if (!existentes.isEmpty() && !existentes.get(0).getId().equals(id)) {
            throw new ValidationException("descricao", "Já existe uma embalagem com esta descrição");
        }

        embalagem.setDescricao(dto.descricao());
        embalagem.setCusto(dto.custo());
        embalagem.setPesoSuportado(dto.pesoSuportado());

        if (embalagem instanceof EmbalagemPlastica plastica && "PLASTICA".equalsIgnoreCase(dto.tipo())) {
            plastica.setTipoPlastico(dto.tipoPlastico());
            plastica.setReciclavel(dto.reciclavel() != null && dto.reciclavel());

        } else if (embalagem instanceof EmbalagemBiodegradavel bio && "BIODEGRADAVEL".equalsIgnoreCase(dto.tipo())) {
            bio.setMaterial(dto.material());
            bio.setTempoDecomposicaoDias(dto.tempoDecomposicaoDias());

        } else if (!dto.tipo().equalsIgnoreCase(getTipoFromEntity(embalagem))) {
            throw new RuntimeException("Não é possível alterar o tipo de embalagem.");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private String getTipoFromEntity(Embalagem embalagem) {
        if (embalagem instanceof EmbalagemPlastica) return "PLASTICA";
        if (embalagem instanceof EmbalagemBiodegradavel) return "BIODEGRADAVEL";
        return "UNKNOWN";
    }
}