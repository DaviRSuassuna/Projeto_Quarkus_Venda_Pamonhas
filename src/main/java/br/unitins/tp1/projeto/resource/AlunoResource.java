package br.unitins.tp1.projeto.resource;

import java.util.List;

import br.unitins.tp1.projeto.dto.AlunoRequestDTO;
import br.unitins.tp1.projeto.dto.AlunoResponseDTO;
import br.unitins.tp1.projeto.mapper.AlunoMapper;
import br.unitins.tp1.projeto.model.Aluno;
import br.unitins.tp1.projeto.service.AlunoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/alunos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlunoResource {

    @Inject
    AlunoService service;

    @GET
    public Response buscarTodos() {
        List<AlunoResponseDTO> lista = service.findAll()
            .stream()
            .map(AlunoMapper::toResponseDTO)
            .toList();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/find/{nome}")
    public Response buscarPeloNome(@PathParam("nome") String nome) {
        List<AlunoResponseDTO> lista = service.findByNome(nome)
            .stream()
            .map(AlunoMapper::toResponseDTO)
            .toList();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPeloId(@PathParam("id") Long id) {
        Aluno aluno = service.findById(id);
        if (aluno == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(AlunoMapper.toResponseDTO(aluno)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        if (service.findById(id) == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        service.delete(id);
        return Response.noContent().build();
    }

    @POST
    public Response incluir(@Valid AlunoRequestDTO dto) {
        Aluno aluno = service.create(AlunoMapper.toEntity(dto));
        return Response.status(Status.CREATED).entity(AlunoMapper.toResponseDTO(aluno)).build();
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, @Valid AlunoRequestDTO dto) {
        if (service.findById(id) == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        service.update(id, AlunoMapper.toEntity(dto));
        return Response.noContent().build();
    }

}
