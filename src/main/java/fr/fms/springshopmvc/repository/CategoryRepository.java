package fr.fms.springshopmvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.fms.springshopmvc.entity.Category;

/**
 * Repository Spring Data JPA pour l'entité Category.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Renvoie les catégories triées par nom croissant.
     */
    List<Category> findAllByOrderByNameAsc();

    /**
     * Renvoie les catégories triées par nom décroissant.
     */
    List<Category> findAllByOrderByNameDesc();
}