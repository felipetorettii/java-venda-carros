/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.esucri.vendacarros.services;

import br.com.esucri.vendacarros.entities.Compra;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author felip
 */
@Stateless
public class CompraService {
    
    @PersistenceContext(unitName = "CarrosPU")
    private EntityManager entityManager;
    
    public Compra findById(Long id) {
        return entityManager.find(Compra.class, id);
    }

    public Compra add(Compra compra) {
        entityManager.persist(compra);
        return compra;
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
