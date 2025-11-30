package com.grecfinances.controller;

import com.grecfinances.model.CategoriaModel;
import com.grecfinances.model.LancamentoModel;
import com.grecfinances.model.LancamentoRequestDTO;
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
    public ResponseEntity<LancamentoModel> salvarLancamento(@RequestBody LancamentoRequestDTO lancamentoDTO,
                                                            @SessionAttribute("usuarioLogado") UsuarioModel usuario) {
        Optional<CategoriaModel> categoriaOpt = categoriaRepository.findById(lancamentoDTO.getCategoriaId());
        if (categoriaOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        LancamentoModel lancamento = new LancamentoModel();
        lancamento.setDescricao(lancamentoDTO.getDescricao());
        lancamento.setValor(lancamentoDTO.getValor());
        lancamento.setTipo(lancamentoDTO.getTipo());
        lancamento.setData(lancamentoDTO.getData());
        lancamento.setCategoria(categoriaOpt.get());
        lancamento.setUsuario(usuario);

        LancamentoModel savedLancamento = lancamentoRepository.save(lancamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLancamento);
    }
}

