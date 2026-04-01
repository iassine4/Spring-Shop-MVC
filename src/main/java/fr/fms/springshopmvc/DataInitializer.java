package fr.fms.springshopmvc;

import fr.fms.springshopmvc.entity.Article;
import fr.fms.springshopmvc.repository.ArticleRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Insère quelques données de test au démarrage.
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ArticleRepository articleRepository) {
        return args -> {
            // Insertion d'un petit jeu d'essai
          //  articleRepository.save(new Article(null, "Smartphone Galaxy S24", "Samsung", 899.99));
          //  articleRepository.save(new Article(null, "TV OLED 55 pouces", "LG", 1199.99));
          //  articleRepository.save(new Article(null, "Laptop Inspiron", "Dell", 749.99));
           // articleRepository.save(new Article(null, "Smartphone Galaxy S24", "Samsung", 899.99));
           // articleRepository.save(new Article(null, "TV OLED 55 pouces", "LG", 1199.99));
           // articleRepository.save(new Article(null, "Laptop Inspiron", "Dell", 749.99));
           // articleRepository.save(new Article(null, "iPhone 15", "Apple", 1099.99));
           // articleRepository.save(new Article(null, "Monitor 27 pouces", "AOC", 229.99));
           // articleRepository.save(new Article(null, "Mouse MX Master", "Logitech", 99.99));
            //articleRepository.save(new Article(null, "Keyboard K95", "Corsair", 179.99));
            //articleRepository.save(new Article(null, "Tablet Galaxy Tab", "Samsung", 499.99));
        };
    }
}
