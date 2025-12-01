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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
                              @RequestParam(name = "search", required = false) String search,
                              @RequestParam(name = "tipo", required = false) String tipo,
                              @RequestParam(name = "categoriaId", required = false) String categoriaIdParam, 
                              @RequestParam(name = "mes", required = false) String mesParam,
                              @RequestParam(name = "sort", required = false, defaultValue = "recente") String sort) {
        List<LancamentoModel> lancamentos;
        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasTipo = tipo != null && !tipo.trim().isEmpty();

        Integer mes = null;
        if (mesParam != null && !mesParam.trim().isEmpty()){
            try {
                mes = Integer.parseInt(mesParam.trim());
                if (mes < 1 || mes > 12) mes = null;
            } catch (NumberFormatException e) {
                mes = null;
            }
        }

        // Parse seguro do parametro categoriaId (pode vir como ""), evitando erro de conversão
        Integer categoriaId = null;
        if (categoriaIdParam != null && !categoriaIdParam.trim().isEmpty()) {
            try {
                categoriaId = Integer.parseInt(categoriaIdParam.trim());
            } catch (NumberFormatException e) {
                categoriaId = null;
            }
        }

        boolean hasCategoria = categoriaId != null;
        String searchValue = (search != null) ? search.trim() : "";

        if (hasSearch && hasTipo && hasCategoria) {
            lancamentos = lancamentoRepository.findByUsuarioAndTipoAndCategoria_IdAndDescricaoContainingIgnoreCase(usuario, tipo, categoriaId, searchValue);
        } else if (hasSearch && hasTipo) {
            lancamentos = lancamentoRepository.findByUsuarioAndTipoAndDescricaoContainingIgnoreCase(usuario, tipo, searchValue);
        } else if (hasSearch && hasCategoria) {
            lancamentos = lancamentoRepository.findByUsuarioAndCategoria_IdAndDescricaoContainingIgnoreCase(usuario, categoriaId, searchValue);
        } else if (hasTipo && hasCategoria) {
            lancamentos = lancamentoRepository.findByUsuarioAndTipoAndCategoria_Id(usuario, tipo, categoriaId);
        } else if (hasTipo) {
            lancamentos = lancamentoRepository.findByUsuarioAndTipo(usuario, tipo);
        } else if (hasCategoria) {
            lancamentos = lancamentoRepository.findByUsuarioAndCategoria_Id(usuario, categoriaId);
        } else if (hasSearch) {
            lancamentos = lancamentoRepository.findByUsuarioAndDescricaoContainingIgnoreCase(usuario, searchValue);
        } else {
            lancamentos = lancamentoRepository.findByUsuario(usuario);
        }
        if (mes != null) {
            int mesFiltrado = mes;
            lancamentos.removeIf(l -> l.getData() == null || l.getData().getMonthValue() != mesFiltrado);
        }

        // Lógica de Ordenação
        switch (sort) {
            case "valor":
                lancamentos.sort(Comparator.comparing(LancamentoModel::getValor).reversed());
                break;
            case "velho":
                lancamentos.sort(Comparator.comparing(LancamentoModel::getData));
                break;
            case "nome":
                lancamentos.sort(Comparator.comparing(LancamentoModel::getDescricao, String.CASE_INSENSITIVE_ORDER));
                break;
            case "recente":
            default:
                lancamentos.sort(Comparator.comparing(LancamentoModel::getData).reversed());
                break;
        }

        model.addAttribute("search", search);
        model.addAttribute("tipo", tipo);
        model.addAttribute("categoriaId", categoriaId);
        model.addAttribute("mes", mes);
        model.addAttribute("sort", sort);
        model.addAttribute("sortNames", Map.of(
            "recente", "Mais Recentes",
            "velho", "Mais Antigos",
            "valor", "Valor (maior)",
            "nome", "Nome (A-Z)"
        ));
        model.addAttribute("lancamentos", lancamentos);
        model.addAttribute("total", lancamentos.size());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("nomeUsuario", usuario.getNome());
        return "lancamentos";
    }

    @GetMapping("/api/lancamentos/{id}")
    @ResponseBody
    public LancamentoModel getLancamento(@PathVariable(required = true) Long id) {
        if (id == null) return null;
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

        if (id == null) {
            redirectAttributes.addFlashAttribute("erro", "ID do lançamento não pode ser nulo.");
            return "redirect:/lancamentos";
        }
        Optional<LancamentoModel> lancamentoOpt = lancamentoRepository.findById(id);
        if (lancamentoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Lançamento não encontrado para editar.");
            return "redirect:/lancamentos";
        }

        if (categoriaId == null) {
            redirectAttributes.addFlashAttribute("erro", "ID da categoria não pode ser nulo.");
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

    @PostMapping("/lancamentos/excluir/{id}")
    public String excluirLancamento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute("erro", "ID do lançamento não pode ser nulo.");
            return "redirect:/lancamentos";
        }
        try {
            lancamentoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("sucesso", "Lançamento excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir lançamento: " + e.getMessage());
        }
        return "redirect:/lancamentos";
    }
}