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

    // Novo método: filtrar por tipo
    List<LancamentoModel> findByUsuarioAndTipo(UsuarioModel usuario, String tipo);

    // Novo método: filtrar por categoria
    List<LancamentoModel> findByUsuarioAndCategoria_Id(UsuarioModel usuario, Integer categoriaId);

    // Novo método: filtrar por tipo e categoria
    List<LancamentoModel> findByUsuarioAndTipoAndCategoria_Id(UsuarioModel usuario, String tipo, Integer categoriaId);

    // Novo método: filtrar por tipo, categoria e descrição
    List<LancamentoModel> findByUsuarioAndTipoAndCategoria_IdAndDescricaoContainingIgnoreCase(UsuarioModel usuario, String tipo, Integer categoriaId, String descricao);

    // Novo método: filtrar por tipo e descrição
    List<LancamentoModel> findByUsuarioAndTipoAndDescricaoContainingIgnoreCase(UsuarioModel usuario, String tipo, String descricao);

    // Novo método: filtrar por categoria e descrição
    List<LancamentoModel> findByUsuarioAndCategoria_IdAndDescricaoContainingIgnoreCase(UsuarioModel usuario, Integer categoriaId, String descricao);
}
