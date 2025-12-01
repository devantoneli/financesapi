package com.grecfinances.controller;

import com.grecfinances.model.CategoriaModel;
import com.grecfinances.model.LancamentoModel;
import com.grecfinances.model.UsuarioModel;
import com.grecfinances.repository.CategoriaRepository;
import com.grecfinances.repository.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoControllerPopUp {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @PostMapping
    public ResponseEntity<LancamentoModel> salvarLancamento(@RequestBody LancamentoModel lancamento,
                                                            @SessionAttribute("usuarioLogado") UsuarioModel usuario) {
        Optional<CategoriaModel> categoriaOpt = categoriaRepository.findById(lancamento.getCategoriaId());
        if (categoriaOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        lancamento.setCategoria(categoriaOpt.get());
        lancamento.setUsuario(usuario);

        LancamentoModel savedLancamento = lancamentoRepository.save(lancamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLancamento);
    }
}
