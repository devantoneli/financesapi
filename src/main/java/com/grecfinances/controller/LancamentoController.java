package com.grecfinances.controller;

import com.grecfinances.model.LancamentoModel;
import com.grecfinances.model.UsuarioModel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/lancamentos")
public class LancamentoController {

    @GetMapping("/novo")
    public String novoLancamentoForm() {
        return "novo-lancamento";
    }

    @PostMapping
    public String salvarLancamento(@RequestParam("descricao") String descricao,
                                   @RequestParam("valor") BigDecimal valor,
                                   @RequestParam("tipo") String tipo,
                                   @RequestParam("data") LocalDate data,
                                   @RequestParam("categoria") Integer categoria,
                                   @SessionAttribute(name = "usuarioLogado") UsuarioModel usuario,
                                   RedirectAttributes redirectAttributes) 
    {
        Long usuarioId = usuario.getId();

        LancamentoModel lancamento = new LancamentoModel();
        lancamento.setDescricao(descricao);
        lancamento.setValor(valor);
        lancamento.setTipo(tipo);
        lancamento.setData(data);
        lancamento.setCategoria(categoria);
        lancamento.setUsuario(usuarioId);

        // Lógica para salvar o lançamento no banco de dados
        // Por enquanto, vamos apenas simular o sucesso

        redirectAttributes.addFlashAttribute("sucesso", "Lançamento salvo com sucesso!");

        return "redirect:/lancamentos/novo";
    }
}
