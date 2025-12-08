package com.example.ms_clientes.application.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtil {

    private static Jwt getJwt() {
        return (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public static String obtenerEmailDelToken() {
        Jwt jwt = getJwt();

        if (jwt.getClaim("preferred_username") != null) {
            return jwt.getClaim("preferred_username");
        }
        if (jwt.getClaim("email") != null) {
            return jwt.getClaim("email");
        }
        throw new RuntimeException("No se encontró un email válido en el token JWT.");
    }

    public static String obtenerNombreDelToken() {
        Jwt jwt = getJwt();

        if (jwt.getClaim("name") != null) {
            return jwt.getClaim("name");
        }

        // Azure AD también puede usar "given_name" + "family_name"
        String given = jwt.getClaim("given_name");
        String family = jwt.getClaim("family_name");

        if (given != null || family != null) {
            return (given + " " + family).trim();
        }

        throw new RuntimeException("No se encontró el nombre del usuario en el token JWT.");
    }
}
