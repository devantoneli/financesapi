package com.grecfinances.repository;

import com.grecfinances.model.LancamentoModel;
import com.grecfinances.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LancamentoRepository extends JpaRepository<LancamentoModel, Long> {
    List<LancamentoModel> findByUsuario(UsuarioModel usuario);
    List<LancamentoModel> findByUsuarioAndDescricaoContainingIgnoreCase(UsuarioModel usuario, String descricao);
}
