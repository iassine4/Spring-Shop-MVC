package fr.fms.springshopmvc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente un article de la boutique.
 * Cette classe sera transformée en table par JPA/Hibernate.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    /**
     * Identifiant unique de l'article.
     * Il sera généré automatiquement par la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Description de l'article.
     */
    private String description;

    /**
     * Marque de l'article.
     */
    private String brand;

    /**
     * Prix de l'article.
     */
    private double price;
}