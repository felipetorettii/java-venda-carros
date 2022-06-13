/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.esucri.vendacarros.services;
import br.com.esucri.vendacarros.entities.Vendedor;
import br.com.esucri.vendacarros.exceptions.ErroMessage;
import br.com.esucri.vendacarros.exceptions.RestException;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;

/**
 *
 * @author felip
 */
@Stateless
public class VendedorService {
    
    @PersistenceContext(unitName = "CarrosPU")
    private EntityManager entityManager;

    public Vendedor findById(Long id) {
        Vendedor vendedor = entityManager.find(Vendedor.class, id);
        if (Objects.isNull(vendedor)) {
            throw new RestException(new ErroMessage("Vendedor não encontrado!"), Response.Status.NOT_FOUND);
        }
        return vendedor;
    }

    public Vendedor add(Vendedor vendedor) {
        validaVendedor(vendedor);
        entityManager.persist(vendedor);
        return vendedor;
    }

    public void remove(Long id) {
        entityManager.remove(findById(id));
    }

    public Vendedor update(Long id, Vendedor vendedorAtualizado) {
        validaVendedor(vendedorAtualizado);
        Vendedor vendedorSalvo = findById(id);
        vendedorAtualizado.setId(vendedorSalvo.getId());
        entityManager.merge(vendedorAtualizado);
        return vendedorAtualizado;
    }

    public List<Vendedor> findAll() {   
        return entityManager
                .createQuery("SELECT v FROM Vendedor v", Vendedor.class)
                .getResultList();
    }

    public List<Vendedor> search(String nome) {
        if (Objects.isNull(nome)) {
            throw new RestException(new ErroMessage("Parâmetro nome não informado!"), Response.Status.BAD_REQUEST);
        }
        return entityManager
                .createQuery("SELECT v FROM Vendedor v WHERE LOWER(v.nome) LIKE :nome", Vendedor.class)
                .setParameter("nome", "%" + nome.toLowerCase() + "%")
                .getResultList();
    }
    
    private void validaVendedor(Vendedor vendedor) {
        validaCPF(vendedor);
        validaTelefone(vendedor);
    }
    
    private void validaCPF(Vendedor vendedor) {
        if (Objects.isNull(vendedor.getCpf()) || vendedor.getCpf().length() != 11) {
            throw new RestException(new ErroMessage("CPF inválido!"), Response.Status.BAD_REQUEST);
        }
    }
    
    private void validaTelefone(Vendedor vendedor) {
        if (Objects.isNull(vendedor.getTelefone()) || vendedor.getTelefone().length() < 8) {
            throw new RestException(new ErroMessage("Telefone inválido!"), Response.Status.BAD_REQUEST);
        }
    }
    
}
