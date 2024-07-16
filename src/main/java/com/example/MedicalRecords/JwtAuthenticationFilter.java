package com.example.medicalrecords.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private JwtTokenProvider tokenProvider; //Proveedor de tokens para la generación y validación de tokens

    @Autowired
    private UserDetailsService userDetailsService; //Servicio para cargar los detalles del usuario desde el token

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = getJwtFromRequest(request); //token JWT de la solicitud

        //Si el token no es nulo y es válido
        if (token != null && tokenProvider.validateToken(token)) {
            String username = tokenProvider.getUsernameFromToken(token); //Obtiene el usuario del token
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); 
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); //autenticación con los detalles del usuario
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //Establece los detalles de autenticación basados en la solicitud actual
            SecurityContextHolder.getContext().setAuthentication(authentication); //Establece la autenticación en el contexto de seguridad de Spring
        }

        filterChain.doFilter(request, response); 
    }
//extraer el token JWT del encabezado Authorization de la solicitud
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization"); //Obtiene el valor del encabezado Authorization
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) { //Verifica si el encabezado comienza con "Bearer "
            return bearerToken.substring(7); //Retorna el token JWT sin el prefijo "Bearer ", es decir, quita las primeras 7 imputaciones, 6 de la palabra, una del espacio
        }
        return null; //devuelve nulo si no se encuentra el encabezado de autorización válido
    }
}
