package fr.fms.springshopmvc.controller;

import fr.fms.springshopmvc.entity.Article;
import fr.fms.springshopmvc.repository.ArticleRepository;
import fr.fms.springshopmvc.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    /**
     * Injection du repository par constructeur.
     * Spring fournit automatiquement l'objet nécessaire.
     */
    public ArticleController(ArticleRepository articleRepository,  CategoryRepository categoryRepository) {

        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Affiche la liste paginée des articles
     * avec ou sans filtre sur la description.
     */
    @GetMapping("/index")
    public String showArticles(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "categoryId", required = false) Long categoryId) {

        Page<Article> articlePage;

        // Cas 1 : aucune catégorie sélectionnée
        if (categoryId == null) {
            if (keyword == null || keyword.trim().isEmpty()) {
                articlePage = articleRepository.findAll(PageRequest.of(page, PAGE_SIZE));
            } else {
                articlePage = articleRepository.findByDescriptionContainingIgnoreCase(
                        keyword,
                        PageRequest.of(page, PAGE_SIZE)
                );
            }
        } else {
            // Cas 2 : catégorie sélectionnée
            if (keyword == null || keyword.trim().isEmpty()) {
                articlePage = articleRepository.findByCategoryId(
                        categoryId,
                        PageRequest.of(page, PAGE_SIZE)
                );
            } else {
                articlePage = articleRepository.findByCategoryIdAndDescriptionContainingIgnoreCase(
                        categoryId,
                        keyword,
                        PageRequest.of(page, PAGE_SIZE)
                );
            }
        }

        // Articles à afficher
        model.addAttribute("articles", articlePage.getContent());

        // Pagination
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());

        // Filtres courants
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategoryId", categoryId);

        // Liste des catégories pour la page
        model.addAttribute("categories", categoryRepository.findAllByOrderByNameAsc());

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
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "categoryId", required = false) Long categoryId){

        // Suppression de l'article en base
        articleRepository.deleteById(id);

        // Redirection vers la liste en gardant le contexte utilisateur
        String redirectUrl = "redirect:/index?page=" + page + "&keyword=" + keyword;
        if (categoryId != null) {
            redirectUrl += "&categoryId=" + categoryId;
        }

        return redirectUrl;
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
