package com.grecfinances.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler)
            throws Exception {
        
        String requestURI = request.getRequestURI();
        
        // Rotas públicas (sem necessidade de autenticação)
        if (requestURI.equals("/") || requestURI.equals("/login") || 
            requestURI.equals("/h2-console") || requestURI.startsWith("/h2-console/") ||
            requestURI.startsWith("/static/") || requestURI.startsWith("/css/") || 
            requestURI.startsWith("/js/") || requestURI.startsWith("/img/") || requestURI.startsWith("/cadastro")) {
            return true;
        }
        
        // Verifica se existe sessão com usuário autenticado
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            // Redireciona para login se não houver sessão
            response.sendRedirect("/login");
            return false;
        }
        
        return true;
    }
}
