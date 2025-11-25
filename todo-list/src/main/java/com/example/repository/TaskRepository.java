package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.example.database.SQLiteConnection;
import com.example.model.Task;

public class TaskRepository {

    /**
     * Insere uma nova tarefa no banco de dados.
     * @param task Objeto tarefa preenchido (title, done, userId)
     * @throws Exception Se houver erro de SQL
     */
    public static void add(Task task) throws Exception {
        String sql = "INSERT INTO tasks(title, done, userId) VALUES(?, ?, ?)";
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setBoolean(2, task.isDone());
            pstmt.setInt(3, task.getUserId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Lista tarefas de um usuário específico.
     * @param userId ID do usuário
     * @return Lista de Task
     * @throws Exception Erros de conexão
     */
    public static List<Task> getByUserId(int userId) throws Exception {
        String sql = "SELECT * FROM tasks WHERE userId = ?";
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Task t = new Task();
                t.setId(rs.getInt("id"));
                t.setTitle(rs.getString("title"));
                t.setDone(rs.getBoolean("done"));
                t.setUserId(rs.getInt("userId"));
                tasks.add(t);
            }
        }
        return tasks;
    }

    /**
     * Lista todas as tarefas (para admin).
     * @return Lista de Task
     * @throws Exception Erros de conexão
     */
    public static List<Task> getAll() throws Exception {
        String sql = "SELECT * FROM tasks";
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Task t = new Task();
                t.setId(rs.getInt("id"));
                t.setTitle(rs.getString("title"));
                t.setDone(rs.getBoolean("done"));
                t.setUserId(rs.getInt("userId"));
                tasks.add(t);
            }
        }
        return tasks;
    }

    /**
     * Atualiza uma tarefa (title e done).
     * @param task Objeto tarefa com id preenchido
     * @throws Exception Erros de conexão
     */
    public static void update(Task task) throws Exception {
        String sql = "UPDATE tasks SET title = ?, done = ? WHERE id = ?";
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setBoolean(2, task.isDone());
            pstmt.setInt(3, task.getId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Remove uma tarefa por id.
     * @param id ID da tarefa
     * @return true se removeu, false se não encontrou
     * @throws Exception Erros de conexão
     */
    public static boolean delete(int id) throws Exception {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}