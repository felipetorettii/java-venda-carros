/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.esucri.vendacarros.controllers;

import br.com.esucri.vendacarros.entities.Compra;
import br.com.esucri.vendacarros.services.CompraService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author felip
 */
@Path("compras")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CompraController {
    
    @Inject
    private CompraService compraService;

    @GET
    public List<Compra> findAll() {
        return this.compraService.findAll();
    }
    
    @GET
    @Path("dash")
    public List<Long> findForDash() {
        return this.compraService.findForDash();
    }

    @GET
    @Path("{id}")
    public Compra findById(@PathParam("id") Long id) {
        return this.compraService.findById(id);
    }

    @POST
    public Compra add(Compra compra) {
        return this.compraService.add(compra);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        this.compraService.remove(id);
    }

    @PUT
    @Path("{id}")
    public Compra update(@PathParam("id") Long id, Compra compraAtualizado) {
        return this.compraService.update(id, compraAtualizado);
    }
    
}
