/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.esucri.vendacarros.services;

import br.com.esucri.vendacarros.entities.Carro;
import br.com.esucri.vendacarros.entities.Compra;
import br.com.esucri.vendacarros.enums.FormaPagamento;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author felip
 */
@Stateless
public class CompraService {
    
    @PersistenceContext(unitName = "CarrosPU")
    private EntityManager entityManager;
    
    public Compra findById(Long id) {
        Compra entity = entityManager.find(Compra.class, id);
        
        if(entity == null) {
            throw new NotFoundException("Compra com o id " + id + " não encontrado");
        }
        
        return entity;
    }

    public Compra add(Compra compra) {
        validaCarro(compra.getCarro());
        validaDesconto(compra);
        
        entityManager.persist(compra);
        return compra;
    }
    
    private void validaCarro(Carro carro) {
        if (carro.getVendido()) {
            throw new WebApplicationException(
                    "O carro já está vendido",
                    Response.Status.CONFLICT
            );            
        }
    }
    
    private void validaDesconto(Compra compra) {
        if ((compra.getFormaPagamento() == FormaPagamento.A_PRAZO) && (compra.getDesconto().compareTo(BigDecimal.valueOf(50l)) == 1 )) {
            throw new WebApplicationException(
                    "Só pode receber desconto maior que 50% pagamento a vista",
                    Response.Status.CONFLICT
            );            
        }
    }

    public void remove(Long id, Compra compra) {
        entityManager.remove(findById(id));
    }

    public Compra update(Compra compraAtualizada) {
        entityManager.merge(compraAtualizada);
        return compraAtualizada;
    }

    public List<Compra> findAll() {   
        return entityManager
                .createQuery("SELECT c FROM Compra c", Compra.class)
                .getResultList();
    }
    
}
