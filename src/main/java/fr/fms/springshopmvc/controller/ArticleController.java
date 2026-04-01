package fr.fms.springshopmvc.controller;

import fr.fms.springshopmvc.entity.Article;
import fr.fms.springshopmvc.repository.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contrôleur MVC chargé de gérer l'affichage des articles.
 */
@Controller
public class ArticleController {

    private final ArticleRepository articleRepository;

    /**
     * Injection du repository par constructeur.
     * Spring fournit automatiquement l'objet nécessaire.
     */
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    /**
     * Affiche une page d'articles.
     *
     * Exemple :
     * /index?page=0
     * /index?page=1
     * /index?page=2
     */
    @GetMapping("/index")
    public String showArticles(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        // Nombre d'articles affichés par page
        int pageSize = 5;

        // On demande à Spring Data une page d'articles
        Page<Article> articlePage = articleRepository.findAll(PageRequest.of(page, pageSize));

        // Liste des articles de la page courante
        model.addAttribute("articles", articlePage.getContent());

        // Numéro de la page courante
        model.addAttribute("currentPage", page);

        // Nombre total de pages
        model.addAttribute("totalPages", articlePage.getTotalPages());

        // Retour vers la vue Thymeleaf
        return "articles";
    }
}
