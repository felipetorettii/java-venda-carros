/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.esucri.vendacarros.controllers;

import br.com.esucri.vendacarros.services.VendedorService;
import br.com.esucri.vendacarros.entities.Vendedor;
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

/**
 *
 * @author felip
 */
@Path("vendedores")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VendedorController {
    
    @Inject
    private VendedorService vendedorService;

    @GET
    public List<Vendedor> findAll() {
        return this.vendedorService.findAll();
    }

    @GET
    @Path("{id}")
    public Vendedor findById(@PathParam("id") Long id) {
        Vendedor vendedor = this.vendedorService.findById(id);
        if (vendedor == null) {
            throw new WebApplicationException("Vendedor não encontrado!", Response.Status.NOT_FOUND);
        }
        return vendedor;
    }

    @POST
    public Vendedor add(Vendedor vendedor) {
        return this.vendedorService.add(vendedor);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        Vendedor vendedor = this.vendedorService.findById(id);
        if (vendedor == null) {
            throw new WebApplicationException("Vendedor não encontrado!", Response.Status.NOT_FOUND);
        }
        this.vendedorService.remove(id, vendedor);
    }

    @PUT
    @Path("{id}")
    public Vendedor update(@PathParam("id") Long id, Vendedor vendedorAtualizado) {
        Vendedor vendedorSalvo = findById(id);
        if (vendedorSalvo == null) {
            throw new WebApplicationException("Vendedor não encontrado!", Response.Status.NOT_FOUND);
        }
        vendedorAtualizado.setId(vendedorSalvo.getId());
        return this.vendedorService.update(vendedorAtualizado);
    }

    @GET
    @Path("search")
    public List<Vendedor> search(@QueryParam("nome") String nome) {
        if(nome == null) {
            throw new BadRequestException("Paramêtro nome não informado!");
        }
        return this.vendedorService.search(nome);
    }
}
