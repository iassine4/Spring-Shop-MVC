package fr.fms.springshopmvc.controller;

import fr.fms.springshopmvc.entity.Article;
import fr.fms.springshopmvc.entity.Category;
import fr.fms.springshopmvc.repository.ArticleRepository;
import fr.fms.springshopmvc.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private ArticleController articleController;

    @BeforeEach
    void setUp() {
        articleController = new ArticleController(articleRepository, categoryRepository);
    }

    @Test
    void shouldShowArticlesWithoutKeywordAndWithoutCategory() {
        Article article = new Article(1L, "Article 1", "Marque A", 10.0, null);
        List<Article> articles = Collections.singletonList(article);
        Page<Article> articlePage = new PageImpl<>(articles, PageRequest.of(0, 5), articles.size());
        Category category = new Category(1L, "Catégorie A", Collections.emptyList());

        given(articleRepository.findAll(PageRequest.of(0, 5))).willReturn(articlePage);
        given(categoryRepository.findAllByOrderByNameAsc()).willReturn(Collections.singletonList(category));

        Model model = new ConcurrentModel();
        String viewName = articleController.showArticles(model, 0, "", null);

        assertThat(viewName).isEqualTo("articles");
        assertThat(model.getAttribute("articles")).isEqualTo(articles);
        assertThat(model.getAttribute("currentPage")).isEqualTo(0);
        assertThat(model.getAttribute("totalPages")).isEqualTo(articlePage.getTotalPages());
        assertThat(model.getAttribute("keyword")).isEqualTo("");
        assertThat(model.getAttribute("selectedCategoryId")).isNull();
        assertThat(model.getAttribute("categories")).isEqualTo(Collections.singletonList(category));
    }

    @Test
    void shouldShowArticlesWithKeyword() {
        Article article = new Article(2L, "Article keyword", "Marque B", 12.0, null);
        List<Article> articles = Collections.singletonList(article);
        Page<Article> articlePage = new PageImpl<>(articles, PageRequest.of(0, 5), articles.size());
        Category category = new Category(1L, "Catégorie A", Collections.emptyList());

        given(articleRepository.findByDescriptionContainingIgnoreCase(eq("book"), any(PageRequest.class)))
                .willReturn(articlePage);
        given(categoryRepository.findAllByOrderByNameAsc()).willReturn(Collections.singletonList(category));

        Model model = new ConcurrentModel();
        String viewName = articleController.showArticles(model, 0, "book", null);

        assertThat(viewName).isEqualTo("articles");
        assertThat(model.getAttribute("articles")).isEqualTo(articles);
        assertThat(model.getAttribute("currentPage")).isEqualTo(0);
        assertThat(model.getAttribute("totalPages")).isEqualTo(articlePage.getTotalPages());
        assertThat(model.getAttribute("keyword")).isEqualTo("book");
        assertThat(model.getAttribute("selectedCategoryId")).isNull();
        assertThat(model.getAttribute("categories")).isEqualTo(Collections.singletonList(category));
    }

    @Test
    void shouldShowArticlesWithCategoryOnly() {
        Article article = new Article(3L, "Article cat", "Marque C", 15.0, null);
        List<Article> articles = Collections.singletonList(article);
        Page<Article> articlePage = new PageImpl<>(articles, PageRequest.of(0, 5), articles.size());
        Category category = new Category(1L, "Catégorie A", Collections.emptyList());

        given(articleRepository.findByCategoryId(eq(1L), any(PageRequest.class))).willReturn(articlePage);
        given(categoryRepository.findAllByOrderByNameAsc()).willReturn(Collections.singletonList(category));

        Model model = new ConcurrentModel();
        String viewName = articleController.showArticles(model, 0, "", 1L);

        assertThat(viewName).isEqualTo("articles");
        assertThat(model.getAttribute("articles")).isEqualTo(articles);
        assertThat(model.getAttribute("currentPage")).isEqualTo(0);
        assertThat(model.getAttribute("totalPages")).isEqualTo(articlePage.getTotalPages());
        assertThat(model.getAttribute("keyword")).isEqualTo("");
        assertThat(model.getAttribute("selectedCategoryId")).isEqualTo(1L);
        assertThat(model.getAttribute("categories")).isEqualTo(Collections.singletonList(category));
    }

    @Test
    void shouldShowArticlesWithCategoryAndKeyword() {
        Article article = new Article(4L, "Article pen", "Marque D", 20.0, null);
        List<Article> articles = Collections.singletonList(article);
        Page<Article> articlePage = new PageImpl<>(articles, PageRequest.of(0, 5), articles.size());
        Category category = new Category(1L, "Catégorie A", Collections.emptyList());

        given(articleRepository.findByCategoryIdAndDescriptionContainingIgnoreCase(eq(1L), eq("pen"),
                any(PageRequest.class))).willReturn(articlePage);
        given(categoryRepository.findAllByOrderByNameAsc()).willReturn(Collections.singletonList(category));

        Model model = new ConcurrentModel();
        String viewName = articleController.showArticles(model, 0, "pen", 1L);

        assertThat(viewName).isEqualTo("articles");
        assertThat(model.getAttribute("articles")).isEqualTo(articles);
        assertThat(model.getAttribute("currentPage")).isEqualTo(0);
        assertThat(model.getAttribute("totalPages")).isEqualTo(articlePage.getTotalPages());
        assertThat(model.getAttribute("keyword")).isEqualTo("pen");
        assertThat(model.getAttribute("selectedCategoryId")).isEqualTo(1L);
        assertThat(model.getAttribute("categories")).isEqualTo(Collections.singletonList(category));
    }

    @Test
    void shouldDeleteArticleAndRedirectToIndex() {
        // Given
        Long articleId = 5L;
        int currentPage = 2;
        String keyword = "search";
        Long categoryId = 3L;

        // When
        String redirect = articleController.deleteArticle(articleId, currentPage, keyword, categoryId);

        // Then
        verify(articleRepository).deleteById(articleId);
        assertThat(redirect)
                .isEqualTo("redirect:/index?page=2&keyword=search&categoryId=3");
    }
}
