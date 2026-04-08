package br.unitins.tp1.projeto.service;

import java.util.List;

import br.unitins.tp1.projeto.dto.ClienteRequestDTO;
import br.unitins.tp1.projeto.dto.ClienteResponseDTO;
import br.unitins.tp1.projeto.mapper.ClienteMapper;
import br.unitins.tp1.projeto.model.Cliente;
import br.unitins.tp1.projeto.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ClienteServiceImpl implements ClienteService {

    @Inject
    ClienteRepository clienteRepository;

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll().list();
    }

    @Override
    public Cliente findById(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public List<Cliente> findByNome(String nome) {
        return clienteRepository.findByNome(nome);
    }

    @Override
    public List<Cliente> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    @Override
    public Cliente findByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    @Override
    public List<Cliente> findByTelefone(String telefone) {
        return clienteRepository.findByTelefone(telefone);
    }

    @Override
    @Transactional
    public ClienteResponseDTO create(ClienteRequestDTO dto) {
        Cliente cliente = ClienteMapper.toEntity(dto);

        clienteRepository.persist(cliente);

        return ClienteMapper.toResponseDTO(cliente);
    }

    @Override
    @Transactional
    public void update(Long id, ClienteRequestDTO dto) {
        Cliente cliente = findById(id);

        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado");
        }

        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
        cliente.setCpf(dto.cpf());
        cliente.setDataNascimento(dto.dataNascimento());

        Cliente novoCliente = ClienteMapper.toEntity(dto);
        cliente.setEnderecos(novoCliente.getEnderecos());

        clienteRepository.persist(cliente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }
}