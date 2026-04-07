package br.unitins.tp1.projeto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import br.unitins.tp1.projeto.dto.ClienteRequestDTO;
import br.unitins.tp1.projeto.dto.ClienteResponseDTO;
import br.unitins.tp1.projeto.dto.EnderecoDTO;
import br.unitins.tp1.projeto.model.Cliente;
import br.unitins.tp1.projeto.model.Endereco;

public class ClienteMapper {

    public static Cliente toEntity(ClienteRequestDTO dto) {
        
        if (dto == null) return null;

        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
        cliente.setCpf(dto.cpf());
        cliente.setDataNascimento(dto.dataNascimento());

        
        List<Endereco> enderecos = dto.enderecos().stream()
            .map(e -> new Endereco(
                e.rua(),
                e.numero(),
                e.bairro(),
                e.cidade(),
                e.estado(),
                e.cep()
            ))
            .collect(Collectors.toList());

        cliente.setEnderecos(enderecos);

        return cliente;
    }

    public static ClienteResponseDTO toResponseDTO(Cliente cliente) {

        if (cliente == null) return null;

        List<EnderecoDTO> enderecoDTO = cliente.getEnderecos()
            .stream()
            .map(e -> new EnderecoDTO(
                e.getRua(),
                e.getNumero(),
                e.getBairro(),
                e.getCidade(),
                e.getEstado(),
                e.getCep()
            ))
            .toList();

        return new ClienteResponseDTO(
            cliente.getId(),
            cliente.getNome(),
            cliente.getEmail(),
            cliente.getTelefone(),
            cliente.getCpf(),
            cliente.getDataNascimento(),
            enderecoDTO
        );
    }
}
