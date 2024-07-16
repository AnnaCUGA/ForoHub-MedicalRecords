package com.example.medicalrecords.controller;

import com.example.medicalrecords.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/login") //Mapea todas las solicitudes /login a este controlador
public class AuthController {

    @Autowired //Spring para el AuthenticationManager
    private AuthenticationManager authenticationManager;

    @Autowired//Spring para el JwtTokenProvider
    private JwtTokenProvider tokenProvider;

    //autenticar y generar un token JWT
    @PostMapping
    public String authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        //Autenticación del usuario utilizando el AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), //Nombre de usuario en la solicitud
                        loginRequest.getPassword() //Contraseña
                )
        );

        //Establece el contexto de seguridad con la autenticación válida
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Genera y devuelve un token JWT utilizando JwtTokenProvider
        return tokenProvider.generateToken(((User) authentication.getPrincipal()).getUsername());
    }
}

//Clase utilizada para la solicitud de inicio de sesión
class LoginRequest {
    private String username;
    private String password;

}
