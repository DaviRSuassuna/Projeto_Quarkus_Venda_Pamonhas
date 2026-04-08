package br.unitins.tp1.projeto.repository;

import java.util.List;

import br.unitins.tp1.projeto.model.Pedido;
import br.unitins.tp1.projeto.model.StatusPedido;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PedidoRepository implements PanacheRepository<Pedido> {

    public List<Pedido> findByClienteNome(String nome) {
        return find("SELECT p FROM Pedido p WHERE UPPER(p.cliente.nome) LIKE UPPER(?1)", "%" + nome + "%").list();
    }

    public List<Pedido> findByStatus(StatusPedido status) {
        return find("SELECT p FROM Pedido p WHERE p.status = ?1", status).list();
    }

    public List<Pedido> findByData(String data) {
        return find("SELECT p FROM Pedido p WHERE CAST(p.data AS string) LIKE ?1", "%" + data + "%").list();
    }

    public List<Pedido> findByClienteId(Long idCliente) {
        return find("SELECT p FROM Pedido p WHERE p.cliente.id = ?1", idCliente).list();
    }
}