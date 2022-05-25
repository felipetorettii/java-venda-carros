package br.com.esucri.vendacarros.controllers;

import br.com.esucri.vendacarros.services.CarroService;
import br.com.esucri.vendacarros.entities.Carro;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("carros")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CarroController {

    @Inject
    private CarroService carroService;

    @GET
    public List<Carro> findAll() {
        return this.carroService.findAll();
    }

    @GET
    @Path("{id}")
    public Carro findById(@PathParam("id") Long id) {
        Carro carro = this.carroService.findById(id);
        if (carro == null) {
            throw new WebApplicationException("Carro não encontrado!", Response.Status.NOT_FOUND);
        }
        return carro;
    }

    @POST
    public Carro add(Carro carro) {
        return this.carroService.add(carro);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        Carro carroSalvo = findById(id);
        if (carroSalvo == null) {
            throw new WebApplicationException("Carro não encontrado!", Response.Status.NOT_FOUND);
        }
        this.carroService.remove(id, carroSalvo);
    }

    @PUT
    @Path("{id}")
    public Carro update(@PathParam("id") Long id, Carro carroAtualizado) {
        Carro carroSalvo = findById(id);
        if (carroSalvo == null) {
            throw new WebApplicationException("Carro não encontrado!", Response.Status.NOT_FOUND);
        }
        carroAtualizado.setId(carroSalvo.getId());
        return this.carroService.update(carroAtualizado);
    }

    @GET
    @Path("search")
    public List<Carro> search(@QueryParam("nome") String nome) {
        if(nome == null) {
            throw new BadRequestException("Paramêtro nome não informado!");
        }
        return this.carroService.search(nome);
    }
}
