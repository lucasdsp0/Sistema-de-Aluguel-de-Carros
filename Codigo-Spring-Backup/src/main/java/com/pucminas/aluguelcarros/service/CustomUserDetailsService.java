package com.pucminas.aluguelcarros.service;

import com.pucminas.aluguelcarros.model.Cliente;
import com.pucminas.aluguelcarros.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        String role = "ROLE_" + cliente.getTipoUsuario().name(); // ROLE_CLIENTE ou ROLE_AGENTE

        return new User(
                cliente.getEmail(),
                cliente.getSenha(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
