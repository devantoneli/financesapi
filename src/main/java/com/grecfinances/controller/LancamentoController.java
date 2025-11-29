package com.grecfinances.controller;

import com.grecfinances.model.CategoriaModel;
import com.grecfinances.model.LancamentoModel;
import com.grecfinances.model.UsuarioModel;
import com.grecfinances.repository.CategoriaRepository;
import com.grecfinances.repository.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/lancamentos")
public class LancamentoController {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/novo")
    public String novoLancamentoForm(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "novo-lancamento";
    }

    @PostMapping
    public String salvarLancamento(@RequestParam("descricao") String descricao,
                                   @RequestParam("valor") BigDecimal valor,
                                   @RequestParam("tipo") String tipo,
                                   @RequestParam("data") LocalDate data,
                                   @RequestParam("categoria") Integer categoriaId,
                                   @SessionAttribute(name = "usuarioLogado") UsuarioModel usuario,
                                   RedirectAttributes redirectAttributes)
    {
        Optional<CategoriaModel> categoriaOpt = categoriaRepository.findById(categoriaId);
        if (categoriaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Categoria não encontrada!");
            return "redirect:/lancamentos/novo";
        }

        LancamentoModel lancamento = new LancamentoModel();
        lancamento.setDescricao(descricao);
        lancamento.setValor(valor);
        lancamento.setTipo(tipo);
        lancamento.setData(data);
        lancamento.setCategoria(categoriaOpt.get());
        lancamento.setUsuario(usuario);

        lancamentoRepository.save(lancamento);

        redirectAttributes.addFlashAttribute("sucesso", "Lançamento salvo com sucesso!");

        return "redirect:/lancamentos/novo";
    }
}

