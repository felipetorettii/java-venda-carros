package br.com.esucri.vendacarros.services;

import br.com.esucri.vendacarros.entities.Carro;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Stateless
public class CarroService {

    @PersistenceContext(unitName = "CarrosPU")
    private EntityManager entityManager;

    public Carro findById(Long id) {
        Carro entity = entityManager.find(Carro.class, id);
        
        if(entity == null) {
            throw new NotFoundException("Carro com o id " + id + " não encontrado");
        }
        
        return entity;
    }

    public Carro add(Carro carro) {
        validaValorCarro(carro);
        entityManager.persist(carro);
        return carro;
    }
    
    private void validaValorCarro(Carro carro) {
        if(carro.getPreco().compareTo(BigDecimal.ZERO) != 1) {
            throw new WebApplicationException(
                    "O preço do carro deve ser maior que 0",
                    Response.Status.CONFLICT
            );   
        }
    }

    public void remove(Long id, Carro carro) {
        entityManager.remove(findById(id));
    }

    public Carro update(Carro carroAtualizado) {
        validaValorCarro(carroAtualizado);
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
