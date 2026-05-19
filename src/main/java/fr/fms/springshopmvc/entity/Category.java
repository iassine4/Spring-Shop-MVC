package fr.fms.springshopmvc.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

/**
 * Représente une catégorie d'articles.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    /**
     * Identifiant unique de la catégorie.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom de la catégorie.
     */
    @NotBlank(message = "Le nom de la catégorie est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom de la catégorie doit contenir entre 2 et 50 caractères")
    private String name;

    /**
     * Liste des articles appartenant à cette catégorie.
     *
     * mappedBy = "category" signifie que la relation est pilotée
     * par l'attribut category dans la classe Article.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    //(Exclude) éviter les boucles infinies avec Lombok
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Article> articles = new ArrayList<>();
}