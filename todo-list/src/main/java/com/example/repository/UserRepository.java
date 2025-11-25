package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

import com.example.database.SQLiteConnection;
import com.example.model.Role;
import com.example.model.User;

public class UserRepository {

    /**
     * Insere um novo usuário no banco de dados (com senha hasheada).
     * @param user Objeto usuário preenchido (username, password, role)
     * @throws Exception Se houver erro de SQL ou duplicação de username
     */
    public static void add(User user) throws Exception {
        // Hashea a senha antes de salvar
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword); // Atualiza o objeto temporariamente

        String sql = "INSERT INTO users(username, password, role) VALUES(?, ?, ?)";
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword); // Usa o hash
            pstmt.setString(3, user.getRole().name());
            pstmt.executeUpdate();
        }
    }

    /**
     * Remove um usuário pelo nome de usuário.
     * @param username Nome do usuário a ser removido
     * @return true se apagou, false se não encontrou
     * @throws Exception Erros de conexão
     */
    public static boolean delete(String username) throws Exception {
        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Valida se o par username/senha existe no banco (compara hashes).
     * @param username Nome do usuário
     * @param password Senha em texto plano (será hasheada pra comparação)
     * @return true se as credenciais forem válidas
     * @throws Exception Erros de conexão
     */
    public static boolean validate(String username, String password) throws Exception {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                // Compara a senha entrada com o hash armazenado
                return BCrypt.checkpw(password, hashedPassword);
            }
        }
        return false;
    }

    /**
     * Busca um usuário pelo nome de usuário (útil para Sessão).
     * @param username Nome do usuário
     * @return User ou null se não encontrado
     * @throws Exception Erros de conexão
     */
    public static User getByUsername(String username) throws Exception {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(Role.valueOf(rs.getString("role"))); // Converte string pra enum
                user.setToken(rs.getString("token"));
            }
        }
        return user;
    }

    /**
     * Atualiza o token de um usuário (usado no login).
     * @param username Nome do usuário
     * @param token Token gerado
     * @throws Exception Erros de conexão
     */
    public static void updateToken(String username, String token) throws Exception {
        String sql = "UPDATE users SET token = ? WHERE username = ?";
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, token);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    /**
     * Limpa o token de um usuário.
     * @param token Token gerado
     * @throws Exception Erros de conexão
     */
    public static void removeToken(String token) throws Exception {
        String sql = "UPDATE users SET token = '' WHERE token = ?";
        try (Connection conn = SQLiteConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, token);
            pstmt.executeUpdate();
        }
    }

    /**
     * Busca um usuário pelo token (validação de acesso).
     * @param token Token do usuário
     * @return User ou null se inválido
     * @throws Exception Erros de conexão
     */
    public static User getUserByToken(String token) throws Exception {
        String sql = "SELECT * FROM users WHERE token = ?";
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, token);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setRole(Role.valueOf(rs.getString("role")));
                return u;
            }
        }
        return null; // Token inválido ou não encontrado
    }

    /**
     * Lista todos os usuários (para admin).
     * @return Lista de User (sem senha)
     * @throws Exception Erros de conexão
     */
    public static List<User> getAllUsers() throws Exception {
        String sql = "SELECT id, username, role FROM users"; // Sem senha por segurança
        List<User> users = new ArrayList<>();
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setRole(Role.valueOf(rs.getString("role")));
                users.add(u);
            }
        }
        return users;
    }
}