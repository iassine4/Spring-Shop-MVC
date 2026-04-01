package fr.fms.springshopmvc.controller;

import fr.fms.springshopmvc.repository.ArticleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
     * Affiche la liste des articles.
     * L'URL /index appellera cette méthode.
     */
    @GetMapping("/index")
    public String showArticles(Model model) {

        // On récupère tous les articles en base
        model.addAttribute("articles", articleRepository.findAll());

        // On retourne le nom de la vue Thymeleaf : articles.html
        return "articles";
    }
}
