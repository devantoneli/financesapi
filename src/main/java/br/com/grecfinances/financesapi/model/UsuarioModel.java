package br.com.grecfinances.financesapi.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransacaoModel> listaDeTransacoes = new ArrayList<>();

    public UsuarioModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public List<TransacaoModel> getListaDeTransacoes() { return listaDeTransacoes; }
    public void setListaDeTransacoes(List<TransacaoModel> listaDeTransacoes) {
        this.listaDeTransacoes = listaDeTransacoes;
    }
}
