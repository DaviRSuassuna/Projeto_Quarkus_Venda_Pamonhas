package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.ClienteRequestDTO;
import br.unitins.tp1.projeto.dto.ClienteResponseDTO;
import br.unitins.tp1.projeto.model.Cliente;

public interface ClienteService {

    List<Cliente> findAll();

    Cliente findById(Long id);

    List<Cliente> findByNome(String nome);

    List<Cliente> findByEmail(String email);

    Cliente findByCpf(String cpf);

    List<Cliente> findByTelefone(String telefone);

    ClienteResponseDTO create(ClienteRequestDTO dto);

    void update(Long id, ClienteRequestDTO dto);

    void delete(Long id);
}