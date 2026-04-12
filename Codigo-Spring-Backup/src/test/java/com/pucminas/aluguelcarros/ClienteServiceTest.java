package com.pucminas.aluguelcarros;

import com.pucminas.aluguelcarros.model.Cliente;
import com.pucminas.aluguelcarros.model.EntidadeEmpregadora;
import com.pucminas.aluguelcarros.model.TipoUsuario;
import com.pucminas.aluguelcarros.repository.ClienteRepository;
import com.pucminas.aluguelcarros.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clienteValido;

    @BeforeEach
    void setUp() {
        clienteValido = new Cliente();
        clienteValido.setId(1L);
        clienteValido.setNome("João Silva");
        clienteValido.setCpf("111.444.777-35");
        clienteValido.setRg("MG-12.345.678");
        clienteValido.setEndereco("Rua das Flores, 123");
        clienteValido.setProfissao("Engenheiro");
        clienteValido.setEmail("joao@email.com");
        clienteValido.setSenha("senha123");
        clienteValido.setTipoUsuario(TipoUsuario.CLIENTE);
    }

    @Test
    void deveCadastrarClienteComSucesso() {
        when(clienteRepository.existsByCpf(any())).thenReturn(false);
        when(clienteRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("senhaHash");
        when(clienteRepository.save(any())).thenReturn(clienteValido);

        Cliente salvo = clienteService.cadastrar(clienteValido);

        assertNotNull(salvo);
        assertEquals("João Silva", salvo.getNome());
        verify(clienteRepository, times(1)).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaCadastrado() {
        when(clienteRepository.existsByCpf(clienteValido.getCpf())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clienteService.cadastrar(clienteValido));

        assertEquals("CPF já cadastrado no sistema.", ex.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaCadastrado() {
        when(clienteRepository.existsByCpf(any())).thenReturn(false);
        when(clienteRepository.existsByEmail(clienteValido.getEmail())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clienteService.cadastrar(clienteValido));

        assertEquals("E-mail já cadastrado no sistema.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoComMaisDeTresEmpregadoras() {
        when(clienteRepository.existsByCpf(any())).thenReturn(false);
        when(clienteRepository.existsByEmail(any())).thenReturn(false);

        for (int i = 0; i < 4; i++) {
            EntidadeEmpregadora e = new EntidadeEmpregadora();
            e.setNomeEntidade("Empresa " + i);
            e.setRendimento(BigDecimal.valueOf(1000));
            clienteValido.getEntidadesEmpregadoras().add(e);
        }

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clienteService.cadastrar(clienteValido));

        assertEquals("Máximo de 3 entidades empregadoras permitidas.", ex.getMessage());
    }

    @Test
    void deveBuscarClientePorEmail() {
        when(clienteRepository.findByEmail("joao@email.com"))
                .thenReturn(Optional.of(clienteValido));

        Optional<Cliente> resultado = clienteService.buscarPorEmail("joao@email.com");

        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
    }

    @Test
    void deveExcluirClienteComSucesso() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);

        assertDoesNotThrow(() -> clienteService.excluir(1L));
        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoExcluirClienteInexistente() {
        when(clienteRepository.existsById(99L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> clienteService.excluir(99L));

        assertEquals("Cliente não encontrado.", ex.getMessage());
    }
}
