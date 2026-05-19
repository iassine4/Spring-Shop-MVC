package fr.fms.springshopmvc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

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
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Description de l'article.
     */
    @NotBlank(message = "La description est obligatoire")
    @Size(min = 2, max = 100, message = "La description doit contenir entre 2 et 100 caractères")
    private String description;

    /**
     * Marque de l'article.
     */
    @NotBlank(message = "La marque est obligatoire")
    @Size(min = 2, max = 50, message = "La marque doit contenir entre 2 et 50 caractères")
    private String brand;

    /**
     * Prix de l'article.
     */
    @Min(value = 1, message = "Le prix doit être supérieur à 0")
    private double price;

    /**
     * Catégorie associée à l'article.
     *
     * Plusieurs articles peuvent appartenir à une même catégorie.
     * La colonne category_id sera créée dans la table article.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Category category;
}