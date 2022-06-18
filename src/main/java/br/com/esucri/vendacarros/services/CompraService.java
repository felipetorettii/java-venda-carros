package br.com.esucri.vendacarros.services;

import br.com.esucri.vendacarros.entities.Carro;
import br.com.esucri.vendacarros.entities.Compra;
import br.com.esucri.vendacarros.entities.Vendedor;
import br.com.esucri.vendacarros.enums.FormaPagamento;
import br.com.esucri.vendacarros.exceptions.ErroMessage;
import br.com.esucri.vendacarros.exceptions.RestException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
        validacoesCompra(compra);
        entityManager.persist(compra);
        atualizaCarro(compra);
        return compra;
    }

    public void remove(Long id) {
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
    
    public List<Long> findForDash() {
        List<Compra> compras = entityManager
                .createQuery("SELECT c FROM Compra c", Compra.class)
                .getResultList();
        List<Long> vendasPorMes = new ArrayList<>();
        initializeList(vendasPorMes);
        compras.forEach(c -> {
            Integer mesDaCompra = c.getDataCompra().getMonthValue()-1;
            Long incVendaMes = vendasPorMes.get(mesDaCompra)+1;
            vendasPorMes.set(mesDaCompra, incVendaMes);
        });
        return vendasPorMes;
    }
    
    private void initializeList(List<Long> lista) {
        for (int i = 1; i <= 12; i++) {
            lista.add(0L);
        }
    }
     
    
    private void validacoesCompra(Compra compra) {
        validaVendedor(compra.getVendedor());
        validaCarro(compra.getCarro());
        validaCompra(compra);
    }
    
    private void validaVendedor(Vendedor vendedor) {
        Vendedor vendedorSalvo = entityManager.find(Vendedor.class, vendedor.getId());
        if (Objects.isNull(vendedorSalvo)) {
            throw new RestException(new ErroMessage("Vendedor não encontrado!"), Response.Status.BAD_REQUEST);
        }
    }
    
    private void validaCarro(Carro carro) {
        Carro carroSalvo = entityManager.find(Carro.class, carro.getId());
        if (Objects.isNull(carroSalvo)) {
            throw new RestException(new ErroMessage("Carro não encontrado!"), Response.Status.BAD_REQUEST);
        }
        if (carroSalvo.getVendido()) {
            throw new RestException(new ErroMessage("Carro já foi vendido!"), Response.Status.BAD_REQUEST);
        }
    }
    
    private String validaCompra(Compra compra) {
        if ((compra.getFormaPagamento() == FormaPagamento.A_PRAZO) && (compra.getDesconto().compareTo(BigDecimal.ZERO) == 1 )) {
            throw new RestException(new ErroMessage("Desconto só pode ser aplicado à vista!"), Response.Status.BAD_REQUEST);
        }
        if (compra.getDataCompra().isAfter(LocalDate.now())) {
            throw new RestException(new ErroMessage("A data da compra não pode ser futura!"), Response.Status.BAD_REQUEST);
        }
        return "";
    }
    
    private void atualizaCarro(Compra compra) {
        Carro carro = entityManager.find(Carro.class, compra.getCarro().getId());
        carro.setVendido(true);
        entityManager.merge(carro);
    }
    
}
