package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQLiteConnection {
    // String de conexão (cria o arquivo tasks.db na raiz do projeto)
    private static final String URL = "jdbc:sqlite:tasks.db";

    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }

    // Método para criar as tabelas se elas não existirem
    public static void createTables() {
        String sqlUsers = "CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT UNIQUE NOT NULL, " +
                        "password TEXT NOT NULL, " +
                        "role TEXT NOT NULL DEFAULT 'USER', " +
                        "token TEXT)";
        String sqlTasks = "CREATE TABLE IF NOT EXISTS tasks (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "title TEXT NOT NULL, " +
                        "done BOOLEAN DEFAULT FALSE, " +
                        "userId INTEGER NOT NULL, " +
                        "FOREIGN KEY (userId) REFERENCES users(id))";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUsers);
            stmt.execute(sqlTasks);
            System.out.println("Tabelas criadas/verif.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}