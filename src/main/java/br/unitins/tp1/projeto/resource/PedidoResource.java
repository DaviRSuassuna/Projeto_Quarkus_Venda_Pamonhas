package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.PedidoRequestDTO;
import br.unitins.tp1.projeto.dto.PedidoResponseDTO;
import br.unitins.tp1.projeto.mapper.PedidoMapper;
import br.unitins.tp1.projeto.model.StatusPedido;
import br.unitins.tp1.projeto.service.PedidoService;

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

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

    @Inject
    PedidoService service;

    @POST
    @Transactional
    public PedidoResponseDTO incluir(PedidoRequestDTO dto) {
        return service.create(dto);
    }

    @GET
    public List<PedidoResponseDTO> buscarTodos() {
        return service.findAll()
                .stream()
                .map(p -> PedidoMapper.toResponseDTO(p))
                .toList();
    }

    @GET
    @Path("/{id}")
    public PedidoResponseDTO encontrarPorId(@PathParam("id") long id) {
        return PedidoMapper.toResponseDTO(service.findById(id));
    }

    @GET
    @Path("/find/cliente-nome/{nome}")
    public List<PedidoResponseDTO> encontrarPorClienteNome(@PathParam("nome") String nome) {
        return service.findByClienteNome(nome)
                .stream()
                .map(p -> PedidoMapper.toResponseDTO(p))
                .toList();
    }

    @GET
    @Path("/find/status/{idStatus}")
    public List<PedidoResponseDTO> encontrarPorStatus(@PathParam("idStatus") Long idStatus) {
        return service.findByStatus(StatusPedido.valueOf(idStatus))
                .stream()
                .map(p -> PedidoMapper.toResponseDTO(p))
                .toList();
    }

    @GET
    @Path("/find/cliente/{idCliente}")
    public List<PedidoResponseDTO> encontrarPorClienteId(@PathParam("idCliente") Long idCliente) {
        return service.findByClienteId(idCliente)
                .stream()
                .map(p -> PedidoMapper.toResponseDTO(p))
                .toList();
    }

    @GET
    @Path("/find/data/{data}")
    public List<PedidoResponseDTO> encontrarPorData(@PathParam("data") String data) {
        return service.findByData(data)
                .stream()
                .map(p -> PedidoMapper.toResponseDTO(p))
                .toList();
    }

    @PUT
    @Path("/{id}")
    public void atualizar(@PathParam("id") Long id, PedidoRequestDTO dto) {
        service.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") long id) {
        service.delete(id);
    }
}