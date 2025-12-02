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

    // INSERT 6 - O método salvarLancamento() do backend captura o JSON e transforma em um objeto LancamentoModel.
    @PostMapping
    public ResponseEntity<LancamentoModel> salvarLancamento(@RequestBody LancamentoModel lancamento,
                                                            @SessionAttribute("usuarioLogado") UsuarioModel usuario) {
        // INSERT 7 - Verifica se a categoria existe
        Optional<CategoriaModel> categoriaOpt = categoriaRepository.findById(lancamento.getCategoriaId());
        if (categoriaOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        // INSERT 8 - Seta o objeto final com todos os dados completos.
        lancamento.setCategoria(categoriaOpt.get());
        lancamento.setUsuario(usuario);

        
        // INSERT 9 -  lancamentoRepository.save() executa o INSERT via JPA/Hibernate.
        LancamentoModel savedLancamento = lancamentoRepository.save(lancamento);
        // INSERT 10 -  Devolve o lançamento recém-criado com ID.
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLancamento);
    }
}
