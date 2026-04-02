package fr.fms.springshopmvc.controller;

import fr.fms.springshopmvc.entity.Article;
import fr.fms.springshopmvc.repository.ArticleRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * Contrôleur MVC chargé de gérer l'affichage des articles.
 */
@Controller
public class ArticleController {

    // Nombre d'articles affichés par page
    private static final int PAGE_SIZE = 5;

    private final ArticleRepository articleRepository;

    /**
     * Injection du repository par constructeur.
     * Spring fournit automatiquement l'objet nécessaire.
     */
    public ArticleController(ArticleRepository articleRepository) {

        this.articleRepository = articleRepository;
    }

    /**
     * Affiche la liste paginée des articles
     * avec ou sans filtre sur la description.
     */
    @GetMapping("/index")
    public String showArticles(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        Page<Article> articlePage;

        // Si aucun mot-clé n'est saisi, on affiche tous les articles
        if (keyword == null || keyword.trim().isEmpty()) {

            // On demande à Spring Data une page d'articles
            articlePage = articleRepository.findAll(PageRequest.of(page, PAGE_SIZE));
        } else {

            // Sinon on affiche uniquement les articles filtrés par mot-clé
            articlePage = articleRepository.findByDescriptionContainingIgnoreCase(
                    keyword,
                    PageRequest.of(page, PAGE_SIZE)
            );
        }

        // Liste des articles de la page courante
        model.addAttribute("articles", articlePage.getContent());

        // Numéro de la page courante
        model.addAttribute("currentPage", page);

        // Nombre total de pages
        model.addAttribute("totalPages", articlePage.getTotalPages());

        // On renvoie le mot-clé à la vue pour le garder affiché dans l'input
        model.addAttribute("keyword", keyword);

        // Retour vers la vue Thymeleaf
        return "articles";
    }
    /**
     * Affiche un formulaire vide pour ajouter un nouvel article.
     */
    @GetMapping("/addArticle")
    public String addArticle(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            Model model) {

        // On crée un article vide pour le formulaire
        model.addAttribute("article", new Article());

        // Sert à personnaliser le titre de la page
        model.addAttribute("formMode", "add");

        // Sert à revenir à la bonne page après enregistrement
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);

        return "form/article-form";
    }

    /**
     * Supprime un article à partir de son identifiant,
     * puis redirige vers la liste en conservant la page et le mot-clé.
     */
    @GetMapping("/deleteArticle")
    public String deleteArticle(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        // Suppression de l'article en base
        articleRepository.deleteById(id);

        // Redirection vers la liste en gardant le contexte utilisateur
        return "redirect:/index?page=" + page + "&keyword=" + keyword;
    }
    /**
     * Affiche le formulaire d'édition d'un article existant.
     */
    @GetMapping("/editArticle")
    public String editArticle(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            Model model) {

        Optional<Article> optionalArticle = articleRepository.findById(id);

        if (optionalArticle.isEmpty()) {
            return "redirect:/index?page=" + page + "&keyword=" + keyword;
        }

        // Article à modifier
        model.addAttribute("article", optionalArticle.get());

        // Infos conservées pour revenir à la bonne page après sauvegarde
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);

        return "form/article-form";
    }
    /**
     * Enregistre la mise à jour d'un article.
     */
    @PostMapping("/saveArticle")
    public String saveArticle(
            @Valid @ModelAttribute("article") Article article,
            BindingResult bindingResult,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            Model model) {

        // Si le formulaire contient des erreurs, on réaffiche la page
        if (bindingResult.hasErrors()) {
            model.addAttribute("page", page);
            model.addAttribute("keyword", keyword);
            return "form/article-form";
        }

        // save() fait insert ou update selon que l'id existe déjà ou non
        articleRepository.save(article);

        return "redirect:/index?page=" + page + "&keyword=" + keyword;
    }
}
