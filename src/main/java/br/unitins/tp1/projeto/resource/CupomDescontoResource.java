package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.CupomDescontoRequestDTO;
import br.unitins.tp1.projeto.dto.CupomDescontoResponseDTO;
import br.unitins.tp1.projeto.mapper.CupomDescontoMapper;
import br.unitins.tp1.projeto.model.CupomDesconto;
import br.unitins.tp1.projeto.service.CupomDescontoService;
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

@Path("/cupons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CupomDescontoResource {

    @Inject
    CupomDescontoService service;

    @POST
    @Transactional
    public CupomDescontoResponseDTO incluir(CupomDescontoRequestDTO dto) {
        CupomDesconto cupom = service.create(CupomDescontoMapper.toEntity(dto));
        return CupomDescontoMapper.toResponseDTO(cupom);
    }

    @GET
    public List<CupomDescontoResponseDTO> buscarTodos() {
        return service.findAll()
                .stream()
                .map(c -> CupomDescontoMapper.toResponseDTO(c))
                .toList();
    }

    @GET
    @Path("/{id}")
    public CupomDescontoResponseDTO encontrarPorId(@PathParam("id") Long id) {
        return CupomDescontoMapper.toResponseDTO(service.findById(id));
    }

    @GET
    @Path("/find/ativos")
    public List<CupomDescontoResponseDTO> encontrarAtivos() {
        return service.findByAtivo()
                .stream()
                .map(c -> CupomDescontoMapper.toResponseDTO(c))
                .toList();
    }

    @GET
    @Path("/sort/valor-crescente")
    public List<CupomDescontoResponseDTO> listarPorValorCrescente() {
        return service.listByValorDescontoCrescente()
                .stream()
                .map(c -> CupomDescontoMapper.toResponseDTO(c))
                .toList();
    }

    @GET
    @Path("/sort/valor-decrescente")
    public List<CupomDescontoResponseDTO> listarPorValorDecrescente() {
        return service.listByValorDescontoDecrescente()
                .stream()
                .map(c -> CupomDescontoMapper.toResponseDTO(c))
                .toList();
    }

    @PUT
    @Path("/{id}")
    public void atualizar(@PathParam("id") Long id, CupomDescontoRequestDTO dto) {
        service.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        service.delete(id);
    }
}
