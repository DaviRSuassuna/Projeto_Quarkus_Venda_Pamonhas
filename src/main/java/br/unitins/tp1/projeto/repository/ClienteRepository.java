package br.unitins.tp1.projeto.repository;

import java.util.List;

import br.unitins.tp1.projeto.model.Cliente;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClienteRepository implements PanacheRepository<Cliente> {

    public List<Cliente> findByNome(String nome) {
        return find("SELECT c FROM Cliente c WHERE UPPER(c.nome) LIKE UPPER(?1)", "%" + nome + "%").list();
    }

    public List<Cliente> findByEmail(String email) {
        return find("SELECT c FROM Cliente c WHERE UPPER(c.email) LIKE UPPER(?1)", "%" + email + "%").list();
    }

    public Cliente findByCpf(String cpf) {
        return find("SELECT c FROM Cliente c WHERE c.cpf = ?1", cpf).firstResult();
    }

    public List<Cliente> findByTelefone(String telefone) {
        return find("SELECT c FROM Cliente c WHERE c.telefone LIKE ?1", "%" + telefone + "%").list();
    }
}