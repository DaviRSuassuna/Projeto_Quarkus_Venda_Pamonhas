package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.ClienteRequestDTO;
import br.unitins.tp1.projeto.dto.ClienteResponseDTO;
import br.unitins.tp1.projeto.mapper.ClienteMapper;
import br.unitins.tp1.projeto.service.ClienteService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {

    @Inject
    ClienteService service;

    @POST
    @Transactional
    public ClienteResponseDTO incluir(ClienteRequestDTO dto) {
        return service.create(dto);
    }

    @GET
    public List<ClienteResponseDTO> buscarTodos() {
        return service.findAll()
                .stream()
                .map(c -> ClienteMapper.toResponseDTO(c))
                .toList();
    }

    @GET
    @Path("/{id}")
    public ClienteResponseDTO encontrarPorId(@PathParam("id") long id) {
        return ClienteMapper.toResponseDTO(service.findById(id));
    }

    @GET
    @Path("/find/nome/{nome}")
    public List<ClienteResponseDTO> encontrarPorNome(@PathParam("nome") String nome) {
        return service.findByNome(nome)
                .stream()
                .map(c -> ClienteMapper.toResponseDTO(c))
                .toList();
    }

    @GET
    @Path("/find/email/{email}")
    public List<ClienteResponseDTO> encontrarPorEmail(@PathParam("email") String email) {
        return service.findByEmail(email)
                .stream()
                .map(c -> ClienteMapper.toResponseDTO(c))
                .toList();
    }

    @GET
    @Path("/find/cpf/{cpf}")
    public ClienteResponseDTO encontrarPorCpf(@PathParam("cpf") String cpf) {
        return ClienteMapper.toResponseDTO(service.findByCpf(cpf));
    }

    @GET
    @Path("/find/telefone/{telefone}")
    public List<ClienteResponseDTO> encontrarPorTelefone(@PathParam("telefone") String telefone) {
        return service.findByTelefone(telefone)
                .stream()
                .map(c -> ClienteMapper.toResponseDTO(c))
                .toList();
    }

    @PUT
    @Path("/{id}")
    public void atualizar(@PathParam("id") Long id, ClienteRequestDTO dto) {
        service.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") long id) {
        service.delete(id);
    }
}