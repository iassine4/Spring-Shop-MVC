package fr.fms.springshopmvc.repository;

import fr.fms.springshopmvc.entity.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository Spring Data JPA pour l'entité Article.
 * Spring créera automatiquement l'implémentation au démarrage.
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findByDescriptionContainingIgnoreCase(String keyword, Pageable pageable);

}
