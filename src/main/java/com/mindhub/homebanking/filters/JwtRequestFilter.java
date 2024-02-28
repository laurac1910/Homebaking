package com.mindhub.homebanking.filters;

import com.mindhub.homebanking.services.JwtServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter  extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtServices jwtServices;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");// obtenemos el header de la peticion
        String userName = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) { // verificamos que el header no sea nulo y que empiece con "Bearer " (portador)
            jwt = authorizationHeader.substring(7);
            userName = jwtServices.extracUsername(jwt);
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) { //  evitamos la re- autenticacion, verificamos que el username no sea nulo y que el contexto de seguridad no tenga autenticacion
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName); //cargamos los datos del cliente
            if (jwtServices.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken //autenticamos al usuario, utilizamos  sus autoridades y le pasamos el token de autenticacion
                        (   userDetails, null, userDetails.getAuthorities()); // le pasamos el cliente, el token de autenticacion y las autoridades (collection)

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);// seteamos el contexto de seguridad con el token de autenticacion Esto indica que el usuario ha sido autenticado con éxito
        }
        }

        filterChain.doFilter(request, response);
        //permitie que la solicitud continúe su procesamiento normal si la autenticación es exitosa.
    }
}


