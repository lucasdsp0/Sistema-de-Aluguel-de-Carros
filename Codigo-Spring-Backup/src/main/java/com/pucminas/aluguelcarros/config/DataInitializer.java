package com.pucminas.aluguelcarros.config;

import com.pucminas.aluguelcarros.model.Cliente;
import com.pucminas.aluguelcarros.model.EntidadeEmpregadora;
import com.pucminas.aluguelcarros.model.TipoUsuario;
import com.pucminas.aluguelcarros.repository.ClienteRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;

/**
 * Inicializa dados de teste na startup da aplicação Quarkus
 */
@ApplicationScoped
public class DataInitializer {

    @Inject
    ClienteRepository clienteRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    void startup(@Observes StartupEvent event) {
        if (clienteRepository.count() == 0) {
            // Cliente de teste
            Cliente cliente = new Cliente();
            cliente.setNome("João Silva");
            cliente.setCpf("111.444.777-35");
            cliente.setRg("MG-12.345.678");
            cliente.setEndereco("Rua das Flores, 123 - Belo Horizonte/MG");
            cliente.setProfissao("Engenheiro de Software");
            cliente.setEmail("joao@email.com");
            cliente.setSenha(passwordEncoder.encode("123456"));
            cliente.setTipoUsuario(TipoUsuario.CLIENTE);

            EntidadeEmpregadora emp1 = new EntidadeEmpregadora();
            emp1.setNomeEntidade("PUC Minas");
            emp1.setRendimento(new BigDecimal("5000.00"));
            emp1.setCliente(cliente);
            cliente.getEntidadesEmpregadoras().add(emp1);

            EntidadeEmpregadora emp2 = new EntidadeEmpregadora();
            emp2.setNomeEntidade("Tech Solutions Ltda");
            emp2.setRendimento(new BigDecimal("3000.00"));
            emp2.setCliente(cliente);
            cliente.getEntidadesEmpregadoras().add(emp2);

            clienteRepository.save(cliente);

            // Agente de teste
            Cliente agente = new Cliente();
            agente.setNome("Agente Banco Central");
            agente.setCpf("529.982.247-25");
            agente.setRg("MG-98.765.432");
            agente.setEndereco("Av. Afonso Pena, 1000 - Belo Horizonte/MG");
            agente.setProfissao("Analista Financeiro");
            agente.setEmail("agente@banco.com");
            agente.setSenha(passwordEncoder.encode("123456"));
            agente.setTipoUsuario(TipoUsuario.AGENTE);

            clienteRepository.save(agente);

            System.out.println("==============================================");
            System.out.println("  Dados iniciais carregados com sucesso!");
            System.out.println("  Cliente:  joao@email.com   / senha: 123456");
            System.out.println("  Agente:   agente@banco.com / senha: 123456");
            System.out.println("==============================================");
        }
    }
}
