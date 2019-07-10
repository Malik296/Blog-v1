package net.blog.service;

import net.blog.entity.Article;
import net.blog.entity.Category;
import net.blog.entity.Comment;
import net.blog.exception.RedirectToValidUrlException;
import net.blog.form.CommentForm;
import net.blog.model.Items;

import java.util.List;
import java.util.Map;

public interface BusinessService {

    Map<Integer, Category> mapCategories();

    Items<Article> listArticles(int offset, int limit);

    Items<Article> listArticlesByCategory(String urlCategory, int offset, int limit);

    Category findCategoryByUrl(String categoryUrl);

    Items<Article> listArticlesBySearchQuery(String searchQuery, int offset, int limit);

    Article viewArticle(long articleId, String requestUrl) throws RedirectToValidUrlException;

    List <Comment> listComments(long id, int offset, int limit);

    Comment createComment(CommentForm form);

}
