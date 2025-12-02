package com.grecfinances.controller;

import com.grecfinances.model.UsuarioModel;
import com.grecfinances.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private LoginController loginController;

    private UsuarioModel testUser;

    @BeforeEach
    void setUp() {
        testUser = new UsuarioModel();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setSenha("password123");
        testUser.setNome("Test User");
    }

    @Test
    void testLoginPage_shouldReturnLoginPageName() {
        String viewName = loginController.loginPage();
        assertEquals("login", viewName);
    }

    @Test
    void testDoLogin_withValidCredentials_shouldRedirectToHome() {
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        String viewName = loginController.doLogin("test@example.com", "password123", session, redirectAttributes);

        assertEquals("redirect:/home", viewName);
        verify(session, times(1)).setAttribute("usuarioLogado", testUser);
        verify(redirectAttributes, never()).addFlashAttribute(anyString(), anyString());
    }

    @Test
    void testDoLogin_withInvalidPassword_shouldRedirectToLoginWithError() {
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        String viewName = loginController.doLogin("test@example.com", "wrongpassword", session, redirectAttributes);

        assertEquals("redirect:/login", viewName);
        verify(session, never()).setAttribute(anyString(), any());
        verify(redirectAttributes, times(1)).addFlashAttribute("loginError", "Email ou senha inválidos");
    }

    @Test
    void testDoLogin_withNonExistentUser_shouldRedirectToLoginWithError() {
        when(usuarioRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        String viewName = loginController.doLogin("nonexistent@example.com", "anypassword", session, redirectAttributes);

        assertEquals("redirect:/login", viewName);
        verify(session, never()).setAttribute(anyString(), any());
        verify(redirectAttributes, times(1)).addFlashAttribute("loginError", "Email ou senha inválidos");
    }

    @Test
    void testLogout_shouldInvalidateSessionAndRedirectToLogin() {
        String viewName = loginController.logout(session);

        assertEquals("redirect:/login", viewName);
        verify(session, times(1)).invalidate();
    }
}
