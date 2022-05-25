package br.com.esucri.vendacarros.services;

import br.com.esucri.vendacarros.entities.Carro;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CarroService {

    @PersistenceContext(unitName = "CarrosPU")
    private EntityManager entityManager;

    public Carro findById(Long id) {
        return entityManager.find(Carro.class, id);
    }

    public Carro add(Carro carro) {
        entityManager.persist(carro);
        return carro;
    }

    public void remove(Carro carro) {
        entityManager.remove(carro);
    }

    public Carro update(Carro carroAtualizado) {
        entityManager.merge(carroAtualizado);
        return carroAtualizado;
    }

    public List<Carro> findAll() {   
        return entityManager
                .createQuery("SELECT c FROM Carro c", Carro.class)
                .getResultList();
    }

    public List<Carro> search(String nome) {
        return entityManager
                .createQuery("SELECT c FROM Carro c WHERE LOWER(c.nome) LIKE :nome", Carro.class)
                .setParameter("nome", "%" + nome.toLowerCase() + "%")
                .getResultList();
    }

}
