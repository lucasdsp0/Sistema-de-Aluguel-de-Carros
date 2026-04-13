package com.pucminas.aluguelcarros.config;

import com.pucminas.aluguelcarros.model.Automovel;
import com.pucminas.aluguelcarros.model.Cliente;
import com.pucminas.aluguelcarros.model.EntidadeEmpregadora;
import com.pucminas.aluguelcarros.model.TipoUsuario;
import com.pucminas.aluguelcarros.repository.AutomovelRepository;
import com.pucminas.aluguelcarros.repository.ClienteRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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

    @Inject
    AutomovelRepository automovelRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
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

            clienteRepository.persist(cliente);

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

            clienteRepository.persist(agente);

            // Automóveis de teste
            Automovel automovel1 = new Automovel();
            automovel1.setMarca("Toyota");
            automovel1.setModelo("Corolla");
            automovel1.setPlaca("ABC-1234");
            automovel1.setAnoFabricacao(2022);
            automovel1.setProprietarioTipo(TipoUsuario.AGENTE);
            automovel1.setProprietarioId(agente.getId());
            automovel1.setValorDiarioPadrao(new BigDecimal("150.00"));
            automovel1.setDescricao("Sedan confortável e econômico");
            automovelRepository.persist(automovel1);

            Automovel automovel2 = new Automovel();
            automovel2.setMarca("Honda");
            automovel2.setModelo("Civic");
            automovel2.setPlaca("DEF-5678");
            automovel2.setAnoFabricacao(2023);
            automovel2.setProprietarioTipo(TipoUsuario.AGENTE);
            automovel2.setProprietarioId(agente.getId());
            automovel2.setValorDiarioPadrao(new BigDecimal("180.00"));
            automovel2.setDescricao("Sedan esportivo com tecnologia avançada");
            automovelRepository.persist(automovel2);

            Automovel automovel3 = new Automovel();
            automovel3.setMarca("Volkswagen");
            automovel3.setModelo("Gol");
            automovel3.setPlaca("GHI-9012");
            automovel3.setAnoFabricacao(2021);
            automovel3.setProprietarioTipo(TipoUsuario.AGENTE);
            automovel3.setProprietarioId(agente.getId());
            automovel3.setValorDiarioPadrao(new BigDecimal("120.00"));
            automovel3.setDescricao("Hatch econômico para trajetos urbanos");
            automovelRepository.persist(automovel3);

            Automovel automovel4 = new Automovel();
            automovel4.setMarca("Hyundai");
            automovel4.setModelo("HB20");
            automovel4.setPlaca("JKL-3456");
            automovel4.setAnoFabricacao(2022);
            automovel4.setProprietarioTipo(TipoUsuario.AGENTE);
            automovel4.setProprietarioId(agente.getId());
            automovel4.setValorDiarioPadrao(new BigDecimal("130.00"));
            automovel4.setDescricao("Hatch prático com bom custo-benefício");
            automovelRepository.persist(automovel4);

            Automovel automovel5 = new Automovel();
            automovel5.setMarca("Chevrolet");
            automovel5.setModelo("Onix");
            automovel5.setPlaca("MNO-7890");
            automovel5.setAnoFabricacao(2023);
            automovel5.setProprietarioTipo(TipoUsuario.AGENTE);
            automovel5.setProprietarioId(agente.getId());
            automovel5.setValorDiarioPadrao(new BigDecimal("140.00"));
            automovel5.setDescricao("Hatch robusto com excelente desempenho");
            automovelRepository.persist(automovel5);

            System.out.println("  Dados iniciais carregados com sucesso!");
            System.out.println("  Cliente:  joao@email.com   / senha: 123456");
            System.out.println("  Agente:   agente@banco.com / senha: 123456");
            System.out.println("==============================================");
        }
    }
}
