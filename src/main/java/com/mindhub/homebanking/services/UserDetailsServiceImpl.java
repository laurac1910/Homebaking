package com.mindhub.homebanking.services;

import ch.qos.logback.core.net.server.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;
    @Override // SOBREESCRIBIMOS EL METODO loadUserByUsername QUE NOS DEVUELVE EL USERDETAILS
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      var client =  clientRepository.findByEmail(username);
        if (client == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return User
                .withUsername(username)
                .password(client.getPassword())
                .roles("CLIENT")
                .build();

                
    }
}
