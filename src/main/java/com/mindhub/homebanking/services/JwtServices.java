package com.mindhub.homebanking.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServices {
    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();   // Esta es la firma que   genera una clave secreta
    private static final long expiration = 1000 * 60 * 60; // tiempo de expiracion del token , tipo Long

    public Claims extraAllClaims(String token) {  // extraemos todos los claims del token, es decir, todos los datos que se encuentran en el token, los claims son los datos que se encuentran en el payload
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload(); // parseSignedClaims es el metodo que extrae los claims
    }

    public <T> T extraClaim(String token, Function<Claims, T> claimsTFunction) { // extraemos un claim en particular
        final Claims claims = extraAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public String extracUsername(String token) { // extraemos el username del token usando el metodo extraClaim
        return extraClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) { // extraemos la fecha de expiracion del token usando el metodo extraClaim
        return extraClaim(token, Claims::getExpiration);
    }

    public  Boolean isTokenExpired(String token) { // verificamos si el token (date) que extrajimos antes esta expirado
        return extractExpiration(token).before(new Date()); // si la fecha de expiracion es antes de la fecha actual, el token esta expirado
    }

    public  String generateToken (UserDetails userDetails) { //  es un string que nos devuelve el token,
        Map<String, Object> claims = new HashMap<>(); // creamos un mapa con los datos que queremos que se encuentren en el token
        var role = userDetails.getAuthorities().stream().toList().get(0); // obtengo las autoridades del usuario, les hago un stream y las convierto en una lista, luego obtengo el primer elemento de la lista
        claims.put("role", role); // asignamos el rol a el claim
        return createToken(claims, userDetails.getUsername()); // creamos el token

    }

    private String  createToken (Map<String, Object> claims, String username) { // creamos el token
        return Jwts
                .builder() // creamos el token
                .claims(claims)// le asignamos los claims
                .subject(username)// le asignamos el mail del usuario
                .issuedAt(new Date(System.currentTimeMillis()))// le asignamos la fecha de creacion
                .expiration(new Date(System.currentTimeMillis() + expiration))// le asignamos la fecha de expiracion
                .signWith(secretKey)// le asignamos la firma
                .compact();// compactamos el token
    }

public Boolean validateToken (String token, UserDetails userDetails) { // validamos el token
    final String username = extracUsername(token); // extraemos el username del token
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // verificamos que el username del token sea igual al username del usuario y que el token no este expirado
}
}

