package br.com.esucri.vendacarros.services;

import br.com.esucri.vendacarros.entities.Carro;
import br.com.esucri.vendacarros.exceptions.ErroMessage;
import br.com.esucri.vendacarros.exceptions.RestException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;

@Stateless
public class CarroService {

    @PersistenceContext(unitName = "CarrosPU")
    private EntityManager entityManager;

    public Carro findById(Long id) {
        Carro carro = entityManager.find(Carro.class, id);
        if (carro == null) {
            throw new RestException(new ErroMessage("Carro não encontrado!"), Response.Status.NOT_FOUND);
        }
        return carro;
    }

    public Carro add(Carro carro) {
        validaPreco(carro);
        entityManager.persist(carro);
        return carro;
    }
    
    private String validaPreco(Carro carro) {
        if (carro.getPreco().compareTo(BigDecimal.ZERO) != 1) {
            throw new RestException(new ErroMessage("O preço do carro não pode ser menor ou igual a 0!"), Response.Status.BAD_REQUEST);
        }
        return "";
    }
    
    private String validaVendido(Carro carro) {
        if (carro.getVendido()) {
            throw new RestException(new ErroMessage("O carro já foi vendido não podendo ser excluido!"), Response.Status.BAD_REQUEST);
        }
        return "";
    }
    

    public void remove(Long id) {
        Carro carro = findById(id);
        validaVendido(carro);
        entityManager.remove(carro);
    }

    public Carro update(Long id, Carro carroAtualizado) {
        validaPreco(carroAtualizado);
        Carro carroSalvo = findById(id);
        carroAtualizado.setId(carroSalvo.getId());
        entityManager.merge(carroAtualizado);
        return carroAtualizado;
    }

    public List<Carro> findAll() {   
        return entityManager
                .createQuery("SELECT c FROM Carro c", Carro.class)
                .getResultList();
    }

    public List<Carro> search(String nome) {
        if (Objects.isNull(nome)) {
            throw new RestException(new ErroMessage("Parâmetro nome não informado!"), Response.Status.BAD_REQUEST);
        }
        return entityManager
                .createQuery("SELECT c FROM Carro c WHERE LOWER(c.nome) LIKE :nome", Carro.class)
                .setParameter("nome", "%" + nome.toLowerCase() + "%")
                .getResultList();
    }

}
