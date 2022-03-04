package com.revature.app.services;


import com.revature.app.dtos.responses.Principal;
import com.revature.app.util.JwtConfig;
import com.revature.app.util.exceptions.InvalidRequestException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.util.Date;


// *********************************
// GENERATING AND VALIDATING TOKENS
// *********************************

public class TokenService {

    JwtConfig jwtConfig;

    public TokenService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(Principal subject) {

        long now = System.currentTimeMillis(); // number of milliseconds passed since UNIX time
        JwtBuilder tokenBuilder = Jwts.builder()
                                    .setId(subject.getId())
                                    .setIssuer("ers")
                                    .setIssuedAt(new Date(now))
                                    .setExpiration(new Date(now + jwtConfig.getExpiration()))
                                    .setSubject(subject.getUsername())
                                    .claim("role", subject.getRole())
                                    .signWith(jwtConfig.getSigAlg(), jwtConfig.getSigningKey());

        return tokenBuilder.compact();
    }

    public boolean isTokenValid(String token){
        return extractRequesterDetails(token) != null;
    }

    public Principal extractRequesterDetails(String token){

        try{
            Claims claims = Jwts.parser()
                                .setSigningKey(jwtConfig.getSigningKey())
                                .parseClaimsJws(token)
                                .getBody();

            Principal principal = new Principal();
            principal.setId(claims.getId());
            principal.setUsername(claims.getSubject());
            principal.setRole(claims.get("role", String.class));

            return principal;

        } catch (Exception e){
            return null; // todo consider if this is the best exception
        }

    }
}
