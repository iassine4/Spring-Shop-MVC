package fr.fms.springshopmvc;

import fr.fms.springshopmvc.entity.Article;
import fr.fms.springshopmvc.entity.Category;
import fr.fms.springshopmvc.repository.ArticleRepository;

import fr.fms.springshopmvc.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Insère quelques données de test au démarrage.
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ArticleRepository articleRepository, CategoryRepository categoryRepository) {
        return args -> {

            // Évite les doublons à chaque redémarrage
            if (categoryRepository.count() > 0 || articleRepository.count() > 0) {
                return;
            }

            // Création des catégories
            Category phoneCategory = categoryRepository.save(new Category(null, "Phones", null));
            Category tvCategory = categoryRepository.save(new Category(null, "TV", null));
            Category laptopCategory = categoryRepository.save(new Category(null, "Laptops", null));
            Category accessoryCategory = categoryRepository.save(new Category(null, "Accessories", null));

            // Insertion d'un petit jeu d'essai
            articleRepository.save(new Article(null, "Smartphone Galaxy S24", "Samsung", 899.99, phoneCategory));
            articleRepository.save(new Article(null, "Laptop Inspiron", "Dell", 749.99, laptopCategory));
            articleRepository.save(new Article(null, "Smartphone Galaxy S24", "Samsung", 899.99, phoneCategory));
            articleRepository.save(new Article(null, "TV OLED 55 pouces", "LG", 1199.99, tvCategory));
            articleRepository.save(new Article(null, "Laptop Inspiron", "Dell", 749.99, laptopCategory));
            articleRepository.save(new Article(null, "iPhone 15", "Apple", 1099.99, phoneCategory));
            articleRepository.save(new Article(null, "Monitor 27 pouces", "AOC", 229.99, tvCategory));
            articleRepository.save(new Article(null, "Mouse MX Master", "Logitech", 99.99, accessoryCategory));
            articleRepository.save(new Article(null, "Keyboard K95", "Corsair", 179.99, accessoryCategory));
            articleRepository.save(new Article(null, "Tablet Galaxy Tab", "Samsung", 499.99, phoneCategory));
        };
    }
}
