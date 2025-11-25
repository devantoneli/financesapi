package com.example;

import com.example.database.SQLiteConnection;
import com.example.model.Role;
import com.example.model.Task;
import com.example.model.User;
import com.example.repository.TaskRepository;
import com.example.repository.UserRepository;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        SQLiteConnection.createTables();

        // Cria admin padrão se não houver usuários
        try {
            List<User> users = UserRepository.getAllUsers();
            if (users.isEmpty()) {
                User admin = new User("admin", "admin123", Role.ADMIN);
                UserRepository.add(admin);
                System.out.println("Admin padrão criado: username=admin, password=admin123");
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar admin padrão: " + e.getMessage());
        }

        Javalin app = Javalin.create(config -> {
            // Define o Content-Type padrão para todas as respostas
            config.http.defaultContentType = "text/plain; charset=UTF-8";
            
            // Configura arquivos estáticos se necessário
            config.staticFiles.add("/static", Location.CLASSPATH);

            // Se estiver usando arquivos estáticos ou templates, verifique também:
            config.pvt.javaLangErrorHandler((res, error) -> {
                res.setCharacterEncoding("UTF-8");
            });
        }).start(7078);

        app.before("/api/*", ctx -> {
            try {
                String token = ctx.header("Authorization");
                if (token == null || token.isEmpty()) {
                    ctx.status(401).json(Map.of("error", "Token necessário"));
                    ctx.skipRemainingHandlers();
                    return;
                }
                User user = UserRepository.getUserByToken(token);
                if (user == null) {
                    ctx.status(401).json(Map.of("error", "Token inválido"));
                    ctx.skipRemainingHandlers();
                    return;
                }
                ctx.attribute("user", user);
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erro interno: " + e.getMessage()));
                ctx.skipRemainingHandlers();
            }
        });
        app.before("/api/admin/*", ctx -> {
            User user = ctx.attribute("user");
            if (user == null) {
                ctx.status(401).json(Map.of("error", "Usuário não autenticado"));
                ctx.skipRemainingHandlers();
                return;
            }
            if (user.getRole() != Role.ADMIN) {
                ctx.status(403).json(Map.of("error", "Acesso negado"));
                ctx.skipRemainingHandlers();
                return;
            }
        });
        app.post("/login", ctx -> {
            try {
                User loginData = ctx.bodyAsClass(User.class);
                boolean valid = UserRepository.validate(loginData.getUsername(), loginData.getPassword());
                if (!valid) {
                    ctx.status(401).json(Map.of("error", "Credenciais inválidas"));
                    return;
                }
                String token = UUID.randomUUID().toString();
                UserRepository.updateToken(loginData.getUsername(), token);
                ctx.json(Map.of("token", token));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erro interno: " + e.getMessage()));
            }
        });
        app.put("/logout", ctx -> {
            try {
                String token = ctx.header("Authorization");
                if (token != null) {
                    UserRepository.removeToken(token);
                }
                ctx.json(Map.of("message", "Logout OK"));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erro: " + e.getMessage()));
            }
        });
        app.get("/api/admin/users", ctx -> {
            try {
                List<User> users = UserRepository.getAllUsers();
                ctx.json(users);
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erro: " + e.getMessage()));
            }
        });
        app.post("/api/admin/users", ctx -> {
            try {
                User newUser = ctx.bodyAsClass(User.class);
                UserRepository.add(newUser);
                ctx.status(201).json(Map.of("message", "Usuário criado"));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erro: " + e.getMessage()));
            }
        });
        app.delete("/api/admin/users/{username}", ctx -> {
            try {
                String username = ctx.pathParam("username");
                boolean deleted = UserRepository.delete(username);
                if (deleted) {
                    ctx.json(Map.of("message", "Usuário deletado"));
                } else {
                    ctx.status(404).json(Map.of("error", "Usuário não encontrado"));
                }
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erro: " + e.getMessage()));
            }
        });
        app.get("/api/admin/tasks", ctx -> {
            try {
                List<Task> tasks = TaskRepository.getAll();
                ctx.json(tasks);
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erro: " + e.getMessage()));
            }
        });
        app.get("/api/tasks", ctx -> {
            try {
                User user = ctx.attribute("user");
                if (user == null) {
                    ctx.status(401).json(Map.of("error", "Usuário não autenticado"));
                    return;
                }
                List<Task> tasks = TaskRepository.getByUserId(user.getId());
                ctx.json(tasks);
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erro: " + e.getMessage()));
            }
        });
        app.post("/api/tasks", ctx -> {
            try {
                User user = ctx.attribute("user");
                if (user == null) {
                    ctx.status(401).json(Map.of("error", "Usuário não autenticado"));
                    return;
                }
                Task newTask = ctx.bodyAsClass(Task.class);
                newTask.setUserId(user.getId());
                newTask.setDone(false);
                TaskRepository.add(newTask);
                ctx.status(201).json(Map.of("message", "Tarefa criada"));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erro: " + e.getMessage()));
            }
        });
        app.put("/api/tasks/{id}", ctx -> {
            try {
                User user = ctx.attribute("user");
                if (user == null) {
                    ctx.status(401).json(Map.of("error", "Usuário não autenticado"));
                    return;
                }
                int taskId = Integer.parseInt(ctx.pathParam("id"));
                Task updateTask = ctx.bodyAsClass(Task.class);
                updateTask.setId(taskId);
                List<Task> userTasks = TaskRepository.getByUserId(user.getId());
                boolean owns = userTasks.stream().anyMatch(t -> t.getId().equals(taskId));
                if (!owns) {
                    ctx.status(403).json(Map.of("error", "Tarefa não pertence ao usuário"));
                    return;
                }
                TaskRepository.update(updateTask);
                ctx.json(Map.of("message", "Tarefa atualizada"));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erro: " + e.getMessage()));
            }
        });
        app.delete("/api/tasks/{id}", ctx -> {
            try {
                User user = ctx.attribute("user");
                if (user == null) {
                    ctx.status(401).json(Map.of("error", "Usuário não autenticado"));
                    return;
                }
                int taskId = Integer.parseInt(ctx.pathParam("id"));
                List<Task> userTasks = TaskRepository.getByUserId(user.getId());
                boolean owns = userTasks.stream().anyMatch(t -> t.getId().equals(taskId));
                if (!owns) {
                    ctx.status(403).json(Map.of("error", "Tarefa não pertence ao usuário"));
                    return;
                }
                boolean deleted = TaskRepository.delete(taskId);
                if (deleted) {
                    ctx.json(Map.of("message", "Tarefa deletada"));
                } else {
                    ctx.status(404).json(Map.of("error", "Tarefa não encontrada"));
                }
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Erro: " + e.getMessage()));
            }
        });

        System.out.println("Servidor rodando em http://localhost:7078");
    }
}