package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.CategoriaResponseDTO;
import br.unitins.tp1.projeto.dto.ListaDesejosResponseDTO;
import br.unitins.tp1.projeto.dto.PamonhaEcommerceResponseDTO;
import br.unitins.tp1.projeto.model.ListaDesejos;
import br.unitins.tp1.projeto.model.Pamonha;
import br.unitins.tp1.projeto.repository.ListaDesejosRepository;
import br.unitins.tp1.projeto.repository.PamonhaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ListaDesejosServiceImpl implements ListaDesejosService {

    @Inject
    ListaDesejosRepository listaDesejosRepository;

    @Inject
    PamonhaRepository pamonhaRepository;

    @Override
    public ListaDesejosResponseDTO buscarPorUsuario(String login) {
        ListaDesejos lista = listaDesejosRepository.findByUsuario(login);
        if (lista == null) throw new NotFoundException("Lista de desejos não encontrada");
        return toResponseDTO(lista);
    }

    @Override
    @Transactional
    public void adicionarPamonha(String login, Long pamonhaId) {
        ListaDesejos lista = listaDesejosRepository.findByUsuario(login);
        if (lista == null) throw new NotFoundException("Lista de desejos não encontrada");
        Pamonha pamonha = pamonhaRepository.findById(pamonhaId);
        if (pamonha == null) throw new NotFoundException("Pamonha não encontrada");
        boolean jaExiste = lista.getPamonhas().stream()
                .anyMatch(p -> p.getId().equals(pamonhaId));
        if (!jaExiste) lista.getPamonhas().add(pamonha);
    }

    @Override
    @Transactional
    public void removerPamonha(String login, Long pamonhaId) {
        ListaDesejos lista = listaDesejosRepository.findByUsuario(login);
        if (lista == null) throw new NotFoundException("Lista de desejos não encontrada");
        lista.getPamonhas().removeIf(p -> p.getId().equals(pamonhaId));
    }

    private ListaDesejosResponseDTO toResponseDTO(ListaDesejos lista) {
        List<PamonhaEcommerceResponseDTO> pamonhasDto = lista.getPamonhas().stream()
                .map(p -> new PamonhaEcommerceResponseDTO(
                    p.getId(), p.getNome(), p.getDescricao(), p.getPreco(), p.getEstoque(),
                    p.getCategorias().stream()
                        .map(c -> new CategoriaResponseDTO(c.getId(), c.getNome(), c.getDescricao()))
                        .toList()
                ))
                .toList();
        return new ListaDesejosResponseDTO(lista.getId(), pamonhasDto);
    }
}
