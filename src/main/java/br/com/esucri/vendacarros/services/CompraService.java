/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.esucri.vendacarros.services;

import br.com.esucri.vendacarros.entities.Carro;
import br.com.esucri.vendacarros.entities.Compra;
import br.com.esucri.vendacarros.enums.FormaPagamento;
import br.com.esucri.vendacarros.exceptions.ErroMessage;
import br.com.esucri.vendacarros.exceptions.RestException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
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
        Compra compra = entityManager.find(Compra.class, id);
        if (Objects.isNull(compra)) {
            throw new RestException(new ErroMessage("Compra não encontrada!"), Response.Status.NOT_FOUND);
        }
        return compra;
    }

    public Compra add(Compra compra) {
        validaCarro(compra.getCarro());
        validaDesconto(compra);
        entityManager.persist(compra);
        return compra;
    }
    
    private String validaCarro(Carro carro) {
        Carro carroSalvo = entityManager.find(Carro.class, carro.getId());
        if (Objects.isNull(carroSalvo)) {
            throw new RestException(new ErroMessage("Carro não encontrado!"), Response.Status.BAD_REQUEST);
        }
        if (carroSalvo.getVendido()) {
            throw new RestException(new ErroMessage("Carro já foi vendido!"), Response.Status.BAD_REQUEST);
        }
        return "";
    }
    
    private String validaDesconto(Compra compra) {
        if ((compra.getFormaPagamento() == FormaPagamento.A_PRAZO) && (compra.getDesconto().compareTo(BigDecimal.ZERO) == 1 )) {
            throw new RestException(new ErroMessage("Desconto só pode ser aplicado à vista!"), Response.Status.BAD_REQUEST);
        }
        return "";
    }

    public void remove(Long id) {
        findById(id);
        entityManager.remove(findById(id));
    }

    public Compra update(Long id, Compra compraAtualizada) {
        Compra compraSalva = findById(id);
        compraAtualizada.setId(compraSalva.getId());
        entityManager.merge(compraAtualizada);
        return compraAtualizada;
    }

    public List<Compra> findAll() {   
        return entityManager
                .createQuery("SELECT c FROM Compra c", Compra.class)
                .getResultList();
    }
    
}
