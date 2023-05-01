package com.example31._Spring_Security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Authentication authentication) throws IOException, jakarta.servlet.ServletException {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map((authority) -> authority.getAuthority())
                .anyMatch((a) -> a.equals("ROLE_ADMIN"));
        boolean isUser = authentication.getAuthorities().stream()
                .map((authority) -> authority.getAuthority())
                .anyMatch((a) -> a.equals("ROLE_USER"));
        if (isAdmin) {
            response.sendRedirect("only_for_admins");
        } else if (isUser) {
            response.sendRedirect("read_profile");
        } else {
            response.sendRedirect("login");
        }
    }
}