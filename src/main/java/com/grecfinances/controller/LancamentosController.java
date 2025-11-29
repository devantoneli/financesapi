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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class LancamentosController {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/lancamentos")
    public String lancamentos(Model model, 
                              @SessionAttribute(name = "usuarioLogado") UsuarioModel usuario,
                              @RequestParam(name = "search", required = false) String search) {
        List<LancamentoModel> lancamentos;
        if (search != null && !search.trim().isEmpty()) {
            lancamentos = lancamentoRepository.findByUsuarioAndDescricaoContainingIgnoreCase(usuario, search.trim());
            model.addAttribute("search", search);
        } else {
            lancamentos = lancamentoRepository.findByUsuario(usuario);
        }
        
        model.addAttribute("lancamentos", lancamentos);
        model.addAttribute("total", lancamentos.size());
        model.addAttribute("categorias", categoriaRepository.findAll()); // Add categories for the edit form
        return "lancamentos";
    }

    @GetMapping("/api/lancamentos/{id}")
    @ResponseBody
    public LancamentoModel getLancamento(@PathVariable Long id) {
        return lancamentoRepository.findById(id).orElse(null);
    }

    @PostMapping("/lancamentos/editar")
    public String editarLancamento(@RequestParam("id") Long id,
                                   @RequestParam("descricao") String descricao,
                                   @RequestParam("valor") BigDecimal valor,
                                   @RequestParam("tipo") String tipo,
                                   @RequestParam("data") LocalDate data,
                                   @RequestParam("categoria") Integer categoriaId,
                                   RedirectAttributes redirectAttributes) {

        Optional<LancamentoModel> lancamentoOpt = lancamentoRepository.findById(id);
        if (lancamentoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Lançamento não encontrado para editar.");
            return "redirect:/lancamentos";
        }

        Optional<CategoriaModel> categoriaOpt = categoriaRepository.findById(categoriaId);
        if (categoriaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Categoria selecionada é inválida.");
            return "redirect:/lancamentos";
        }

        LancamentoModel lancamento = lancamentoOpt.get();
        lancamento.setDescricao(descricao);
        lancamento.setValor(valor);
        lancamento.setTipo(tipo);
        lancamento.setData(data);
        lancamento.setCategoria(categoriaOpt.get());

        lancamentoRepository.save(lancamento);

        redirectAttributes.addFlashAttribute("sucesso", "Lançamento atualizado com sucesso!");
        return "redirect:/lancamentos";
    }
}