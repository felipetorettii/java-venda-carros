/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.esucri.vendacarros.services;
import br.com.esucri.vendacarros.entities.Vendedor;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author felip
 */
@Stateless
public class VendedorService {
    
    @PersistenceContext(unitName = "CarrosPU")
    private EntityManager entityManager;

    public Vendedor findById(Long id) {
        return entityManager.find(Vendedor.class, id);
    }

    public Vendedor add(Vendedor vendedor) {
        entityManager.persist(vendedor);
        return vendedor;
    }

    public void remove(Vendedor vendedor) {
        entityManager.remove(vendedor);
    }

    public Vendedor update(Vendedor vendedorAtualizado) {
        entityManager.merge(vendedorAtualizado);
        return vendedorAtualizado;
    }

    public List<Vendedor> findAll() {   
        return entityManager
                .createQuery("SELECT v FROM Vendedor v", Vendedor.class)
                .getResultList();
    }

    public List<Vendedor> search(String nome) {
        return entityManager
                .createQuery("SELECT v FROM Vendedor v WHERE LOWER(v.nome) LIKE :nome", Vendedor.class)
                .setParameter("nome", "%" + nome.toLowerCase() + "%")
                .getResultList();
    }
    
}
