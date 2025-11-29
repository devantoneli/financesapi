package com.grecfinances.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nm_usuario")
    private String nome;

    @Column(name = "nm_email")
    private String email;

    @Column(name = "nm_senha")
    private String senha;

    @JsonManagedReference
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LancamentoModel> listaDeLancamentos = new ArrayList<>();

    public UsuarioModel() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<LancamentoModel> getListaDeLancamentos() {
        return listaDeLancamentos;
    }

    public void setListaDeLancamentos(List<LancamentoModel> listaDeLancamentos) {
        this.listaDeLancamentos = listaDeLancamentos;
    }
}
